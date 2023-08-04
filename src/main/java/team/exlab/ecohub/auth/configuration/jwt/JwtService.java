package team.exlab.ecohub.auth.configuration.jwt;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import team.exlab.ecohub.token.TokenPurpose;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtService {

    @Value("${app.accessTokenExpirationMs}")
    private long accessTokenExpirationMs;

    @Value("${app.refreshTokenExpirationMs}")
    private long refreshTokenExpirationMs;

    @Value("${app.refreshTokenRememberMeExpirationMs}")
    private long refreshTokenRememberMeExpirationMs;

    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    public String generateAccessToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", TokenPurpose.ACCESS);
        return generateToken(userDetails, claims, accessTokenExpirationMs);
    }

    public String generateRefreshToken(UserDetails userDetails, boolean rememberMe) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", TokenPurpose.REFRESH);
        claims.put("rememberMe", rememberMe);
        return generateToken(userDetails, claims, rememberMe ? refreshTokenRememberMeExpirationMs : refreshTokenExpirationMs);
    }

    private String generateToken(UserDetails userDetails, Map<String, Object> claims, long expirationTime) {
        return Jwts.builder().setSubject((userDetails.getUsername())).setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + expirationTime))
                .addClaims(claims)
                .signWith(key, SignatureAlgorithm.HS512).compact();
    }

    public boolean validateJwtToken(String jwt) {
        try {
            return Jwts.parser().setSigningKey(key).parseClaimsJws(jwt).getBody().getExpiration().after(new Date());
        } catch (MalformedJwtException | IllegalArgumentException e) {
            log.warn(e.toString());
        }
        return false;
    }


    public String parseJwtFromRequest(HttpServletRequest request) {
        String headerAuth = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }

    public String getUserNameFromJwtToken(String jwt) {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(jwt).getBody().getSubject();
    }

    public String getPurposeFromJwtToken(String jwt) {
        return (String) Jwts.parser().setSigningKey(key).parseClaimsJws(jwt).getBody().get("type");
    }

    public boolean getRememberMeFromJwtToken(String jwt) {
        return (boolean) Jwts.parser().setSigningKey(key).parseClaimsJws(jwt).getBody().get("rememberMe");
    }

}
