package team.exlab.ecohub.auth.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import team.exlab.ecohub.auth.configuration.jwt.JwtService;
import team.exlab.ecohub.exception.JwtTokenException;
import team.exlab.ecohub.exception.UserNotFoundException;
import team.exlab.ecohub.token.TokenService;
import team.exlab.ecohub.user.model.User;
import team.exlab.ecohub.user.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response,
                       Authentication authentication) {
        final String accessToken = jwtService.parseJwtFromRequest(request);
        if (accessToken != null) {
            String username;
            try {
                username = jwtService.getUserNameFromJwt(accessToken);
            } catch (RuntimeException e){
                throw new JwtTokenException(e.getMessage(), e);
            }
            User user = userRepository.findUserByUsername(username).orElseThrow(() ->
                    new UserNotFoundException(username));
            tokenService.revokeRefreshToken(user);
            SecurityContextHolder.clearContext();
        }
    }
}
