package team.exlab.ecohub.token;

import team.exlab.ecohub.user.model.User;

public interface TokenService {
    void saveUserToken(User user, String jwtToken, boolean rememberMe);

    void revokeAllUserTokens(User user);
}
