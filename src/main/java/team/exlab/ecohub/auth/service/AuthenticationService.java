package team.exlab.ecohub.auth.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import team.exlab.ecohub.auth.configuration.jwt.JwtService;
import team.exlab.ecohub.auth.dto.JwtResponseDto;
import team.exlab.ecohub.auth.dto.LoginRequestDto;
import team.exlab.ecohub.auth.dto.MessageResponseDto;
import team.exlab.ecohub.auth.dto.SignupRequestDto;
import team.exlab.ecohub.exception.UserNotFoundException;
import team.exlab.ecohub.token.TokenRepository;
import team.exlab.ecohub.token.TokenService;
import team.exlab.ecohub.user.model.ERole;
import team.exlab.ecohub.user.model.Role;
import team.exlab.ecohub.user.model.User;
import team.exlab.ecohub.user.repository.RoleRepository;
import team.exlab.ecohub.user.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;


    public ResponseEntity<JwtResponseDto> authenticateUser(LoginRequestDto loginRequestDto) {
        Authentication auth = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getUsername(),
                        loginRequestDto.getPassword()));
        User currentUser = userRepository.findUserByUsername(loginRequestDto.getUsername()).orElseThrow();

        SecurityContextHolder.getContext().setAuthentication(auth);

        String accessToken = jwtService.generateAccessToken((UserDetails) auth.getPrincipal());
        String refreshToken = jwtService.generateRefreshToken((UserDetails) auth.getPrincipal(), loginRequestDto.isRememberMe());
        tokenService.revokeAllUserTokens(currentUser);
        tokenService.saveUserToken(currentUser, refreshToken, loginRequestDto.isRememberMe());

        User user = (User) auth.getPrincipal();
        List<String> roles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponseDto(accessToken,
                refreshToken,
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                roles));
    }

    public ResponseEntity<MessageResponseDto> registerUser(SignupRequestDto signupRequestDto) {
        if (userRepository.existsByUsername(signupRequestDto.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponseDto("Error: Username exists"));
        }

        if (signupRequestDto.getEmail() != null && userRepository.existsByEmail(signupRequestDto.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponseDto("Error: Email exists"));
        }

        User user = new User(signupRequestDto.getUsername(),
                passwordEncoder.encode(signupRequestDto.getPassword()),
                signupRequestDto.getEmail()
        );

        ERole reqRole = signupRequestDto.getRole();
        Role role;

        if (reqRole == null) {
            role = roleRepository
                    .findRoleByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error, Role USER is not found"));
        } else {
            switch (reqRole) {
                case ROLE_SUPERADMIN:
                    role = roleRepository
                            .findRoleByName(ERole.ROLE_SUPERADMIN)
                            .orElseThrow(() -> new RuntimeException("Error, Role ADMIN is not found"));
                    break;
                case ROLE_ADMIN:
                    role = roleRepository
                            .findRoleByName(ERole.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Error, Role ADMIN is not found"));
                    break;
                default:
                    role = roleRepository
                            .findRoleByName(ERole.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("Error, Role USER is not found"));
            }
        }
        user.setRole(role);
        userRepository.save(user);
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

            List<String> roles = user.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            try {
                new ObjectMapper().writeValue(response.getOutputStream(), new JwtResponseDto(accessToken,
                        refreshToken,
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        roles));
            } catch (IOException e) {
                log.warn("Error while writing response with new tokens", e);
            }
        }
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
}
