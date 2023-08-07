package team.exlab.ecohub.token;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.exlab.ecohub.user.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final TokenRepository tokenRepository;

    @Override
    @Transactional
    public void saveUserToken(User user, String jwtToken, boolean rememberMe) {
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
    public void revokeAllUserTokens(User user) {
        List<Token> validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty()) {
            return;
        }
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
}
