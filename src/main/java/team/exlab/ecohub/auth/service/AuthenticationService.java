package team.exlab.ecohub.auth.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import team.exlab.ecohub.auth.configuration.jwt.JwtService;
import team.exlab.ecohub.auth.dto.*;
import team.exlab.ecohub.exception.UserNotFoundException;
import team.exlab.ecohub.feedback.repository.FeedbackRepository;
import team.exlab.ecohub.token.TokenRepository;
import team.exlab.ecohub.token.TokenService;
import team.exlab.ecohub.user.model.ERole;
import team.exlab.ecohub.user.model.User;
import team.exlab.ecohub.user.repository.RoleRepository;
import team.exlab.ecohub.user.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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


    public ResponseEntity<JwtResponseDto> authenticateUser(LoginRequestDto loginRequestDto) {
        Authentication auth = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getUsernameOrEmail(),
                        loginRequestDto.getPassword()));
        User currentUser = (User) userService.loadUserByUsername(loginRequestDto.getUsernameOrEmail());
        SecurityContextHolder.getContext().setAuthentication(auth);

        String accessToken = jwtService.generateAccessToken((UserDetails) auth.getPrincipal());
        String refreshToken = jwtService.generateRefreshToken((UserDetails) auth.getPrincipal(), loginRequestDto.isRememberMe());
        tokenService.revokeAllUserTokens(currentUser);
        tokenService.saveUserToken(currentUser, refreshToken, loginRequestDto.isRememberMe());

        User user = (User) auth.getPrincipal();

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

        if (tokenRepository.findByRefreshToken(refreshToken).isPresent() &&
                jwtService.validateRefreshToken(refreshToken)) {

            String username = jwtService.getUserNameFromJwtToken(refreshToken);
            User user = userRepository.findUserByUsername(username)
                    .orElseThrow(() -> new UserNotFoundException(String.format("User with username %s not found", username)));

            String accessToken = jwtService.generateAccessToken(user);
            refreshToken = jwtService.generateRefreshToken(user, jwtService.getRememberMeFromJwtToken(refreshToken));

            tokenService.revokeAllUserTokens(user);
            tokenService.saveUserToken(user, refreshToken, jwtService.getRememberMeFromJwtToken(refreshToken));

            try {
                new ObjectMapper().writeValue(response.getOutputStream(), new JwtResponseDto(accessToken,
                        refreshToken,
                        user.getUsername(),
                        user.getEmail(),
                        user.getRole().getName().name().split("_")[1].toLowerCase()));
            } catch (IOException e) {
                log.warn("Error while writing response with new tokens", e);
            }
        }
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
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

}
