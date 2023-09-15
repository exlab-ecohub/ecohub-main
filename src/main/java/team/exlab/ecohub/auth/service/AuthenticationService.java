package team.exlab.ecohub.auth.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import team.exlab.ecohub.auth.configuration.jwt.JwtService;
import team.exlab.ecohub.auth.dto.*;
import team.exlab.ecohub.exception.AdminBlockedException;
import team.exlab.ecohub.exception.UserNotFoundException;
import team.exlab.ecohub.feedback.repository.FeedbackRepository;
import team.exlab.ecohub.token.Token;
import team.exlab.ecohub.token.TokenRepository;
import team.exlab.ecohub.token.TokenService;
import team.exlab.ecohub.user.model.ERole;
import team.exlab.ecohub.user.model.User;
import team.exlab.ecohub.user.repository.RoleRepository;
import team.exlab.ecohub.user.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final FeedbackRepository feedbackRepository;
    private final UserDetailsService userService;
    private final RoleRepository roleRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;


    public ResponseEntity<JwtResponseDto> authenticate(LoginRequestDto loginRequestDto) {
        User requestedUser = (User) userService.loadUserByUsername(loginRequestDto.getUsernameOrEmail());
        if (requestedUser.isAdmin()) {
            return authenticateAdmin(requestedUser, loginRequestDto);
        } else {
            return authenticateUser(requestedUser, loginRequestDto);
        }
    }

    private ResponseEntity<JwtResponseDto> authenticateAdmin(User admin, LoginRequestDto loginRequestDto) {
        if (!admin.isAccountNonLocked()) {
            throw new AdminBlockedException(admin);
        }
        if (admin.isBlockedBefore() && admin.getPassAttempts() == 0) {
            admin.setPassAttempts(3);
        }
        try {
            Authentication auth = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(
                            loginRequestDto.getUsernameOrEmail(),
                            loginRequestDto.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(auth);
        } catch (AuthenticationException e) {
            handleAdminWrongPassword(admin);
            throw e;
        }

        admin.setPassAttempts(3);
        admin.setBlockedBefore(false);
        userRepository.save(admin);
        return generateResponseWithTokens(admin, loginRequestDto.isRememberMe());
    }

    private ResponseEntity<JwtResponseDto> authenticateUser(User user, LoginRequestDto loginRequestDto) {
        Authentication auth = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getUsernameOrEmail(),
                        loginRequestDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(auth);
        return generateResponseWithTokens(user, loginRequestDto.isRememberMe());
    }

    private ResponseEntity<JwtResponseDto> generateResponseWithTokens(User user, boolean rememberMe) {
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user, rememberMe);
        Optional<Token> token = tokenRepository.findTokenByUserId(user.getId());
        if (token.isPresent()) {
            tokenService.updateRefreshToken(user, passwordEncoder.encode(refreshToken));
        } else {
            tokenService.saveRefreshToken(user, passwordEncoder.encode(refreshToken), rememberMe);
        }

        return ResponseEntity.ok(new JwtResponseDto(accessToken,
                refreshToken,
                user.getUsername(),
                user.getEmail(),
                user.getRole().getName().name().split("_")[1].toLowerCase()));
    }

    public ResponseEntity<MessageResponseDto> registerUser(SignupUserRequestDto signupUserRequestDto) {
        if (userRepository.existsByUsername(signupUserRequestDto.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponseDto("Error: Username exists"));
        }

        if (userRepository.existsByEmail(signupUserRequestDto.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponseDto("Error: Email exists"));
        }

        User user = new User(signupUserRequestDto.getUsername(),
                passwordEncoder.encode(signupUserRequestDto.getPassword()),
                signupUserRequestDto.getEmail(),
                roleRepository
                        .findRoleByName(ERole.ROLE_USER)
                        .orElseThrow(() -> new RuntimeException("Error, Role USER is not found"))
        );

        user = userRepository.save(user);
        updateFeedbacksWithNewUser(user);

        return ResponseEntity.ok(
                new MessageResponseDto(
                        String.format("User %s successfully created", user.getUsername())));
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = jwtService.parseJwtFromRequest(request);

        String username = jwtService.getUserNameFromJwt(refreshToken);
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        Optional<Token> token = tokenRepository.findTokenByUserId(user.getId());

        if (token.isPresent() && token.get().isValid() &&
                passwordEncoder.matches(refreshToken, token.get().getRefreshToken()) &&
                jwtService.validateRefreshToken(refreshToken)) {
            String accessToken = jwtService.generateAccessToken(user);
            refreshToken = jwtService.generateRefreshToken(user, jwtService.getRememberMeFromJwt(refreshToken));

            tokenService.updateRefreshToken(user, passwordEncoder.encode(refreshToken));

            try {
                new ObjectMapper().writeValue(response.getOutputStream(), new JwtResponseDto(accessToken,
                        refreshToken,
                        user.getUsername(),
                        user.getEmail(),
                        user.getRole().getName().name().split("_")[1].toLowerCase()));
            } catch (IOException e) {
                log.warn("Error while writing response with new tokens", e);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    public ResponseEntity<MessageResponseDto> registerAdmin(SignupAdminRequestDto signupAdminRequestDto) {
        if (userRepository.existsByUsername(signupAdminRequestDto.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponseDto("Error: Username exists"));
        }

        User admin = new User(signupAdminRequestDto.getUsername(),
                passwordEncoder.encode(signupAdminRequestDto.getPassword()),
                null,
                roleRepository
                        .findRoleByName(ERole.ROLE_ADMIN)
                        .orElseThrow(() -> new RuntimeException("Error, Role ADMIN is not found"))
        );

        userRepository.save(admin);
        return ResponseEntity.ok(
                new MessageResponseDto(
                        String.format("Admin %s successfully created", admin.getUsername())));
    }

    private void updateFeedbacksWithNewUser(User user) {
        feedbackRepository.findAllByEmail(user.getEmail())
                .forEach(x -> {
                    if (x.getUser() == null) {
                        x.setUser(user);
                        feedbackRepository.save(x);
                    }
                });
    }

    private void handleAdminWrongPassword(User admin) {
        int passAttemptsLeft = admin.getPassAttempts() - 1;
        admin.setPassAttempts(passAttemptsLeft);
        if (passAttemptsLeft == 0) {
            if (admin.isBlockedBefore()) {
                admin.setLockEndTime(LocalDateTime.now().plus(100, ChronoUnit.YEARS));
            } else {
                admin.setLockEndTime(LocalDateTime.now().plus(1, ChronoUnit.HOURS));
                admin.setBlockedBefore(true);
            }
            userRepository.save(admin);
            throw new AdminBlockedException(admin);
        }
        userRepository.save(admin);
    }

}
