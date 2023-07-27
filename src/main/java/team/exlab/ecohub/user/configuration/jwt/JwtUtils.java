package team.exlab.ecohub.user.configuration.jwt;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import team.exlab.ecohub.user.service.UserDetailsImpl;

import java.security.Key;
import java.util.Date;

@Component
@Slf4j
public class JwtUtils {

    @Value("${app.accessTokenExpirationMs}")
    private long accessTokenExpirationMs;

    @Value("${app.refreshTokenExpirationMs}")
    private long refreshTokenExpirationMs;

    @Value("${app.refreshTokenRememberMeExpirationMs}")
    private long refreshTokenRememberMeExpirationMs;

    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    public String generateAccessToken(Authentication authentication) {
        return generateToken(authentication, accessTokenExpirationMs);
    }

    public String generateRefreshToken(Authentication authentication, boolean isRememberMe) {
        return generateToken(authentication, isRememberMe ? refreshTokenRememberMeExpirationMs : refreshTokenExpirationMs);
    }

    private String generateToken(Authentication authentication, long expirationTime) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder().setSubject((userPrincipal.getUsername())).setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + expirationTime))
                .signWith(key, SignatureAlgorithm.HS512).compact();
    }

    public boolean validateJwtToken(String jwt) {
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(jwt);
            return true;
        } catch (MalformedJwtException | IllegalArgumentException e) {
            log.warn(e.toString());
        }
        return false;
    }

    public String getUserNameFromJwtToken(String jwt) {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(jwt).getBody().getSubject();
    }

}
