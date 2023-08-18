package team.exlab.ecohub.token;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.exlab.ecohub.exception.UserNotFoundException;
import team.exlab.ecohub.user.model.User;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final TokenRepository tokenRepository;

    @Override
    @Transactional
    public void saveRefreshToken(User user, String jwtToken, boolean rememberMe) {
        Token token = Token.builder()
                .user(user)
                .refreshToken(jwtToken)
                .tokenType(TokenType.BEARER)
                .tokenPurpose(TokenPurpose.REFRESH)
                .rememberMe(rememberMe)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    @Override
    @Transactional
    public void updateRefreshToken(User user, String jwtToken) {
        Token token = tokenRepository.findTokenByUserId(user.getId())
                .orElseThrow(() -> new UserNotFoundException(user.getUsername()));
        token.setRefreshToken(jwtToken);
        token.setRevoked(false);
        token.setExpired(false);
        tokenRepository.save(token);
    }


    @Override
    @Transactional
    public void revokeRefreshToken(User user) {
        Token validUserToken = tokenRepository.findTokenByUserId(user.getId())
                .orElseThrow(() -> new UserNotFoundException(user.getUsername()));
        validUserToken.setExpired(true);
        validUserToken.setRevoked(true);
        tokenRepository.save(validUserToken);
    }
}
