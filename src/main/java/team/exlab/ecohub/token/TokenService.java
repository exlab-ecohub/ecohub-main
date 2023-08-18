package team.exlab.ecohub.token;

import team.exlab.ecohub.user.model.User;

public interface TokenService {
    void saveRefreshToken(User user, String jwtToken, boolean rememberMe);

    void updateRefreshToken(User user, String jwtToken);

    void revokeRefreshToken(User user);
}
