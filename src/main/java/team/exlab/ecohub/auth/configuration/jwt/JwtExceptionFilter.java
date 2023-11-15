package team.exlab.ecohub.auth.configuration.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtExceptionFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterchain) throws ServletException, IOException {
        try {
            filterchain.doFilter(request, response);
        } catch (JwtException ex) {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            final Map<String, Object> body = new HashMap<>();
            body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
            body.put("error", ex instanceof ExpiredJwtException ? "JWT expired" : "JWT problem");
            body.put("message", ex.getMessage());
            body.put("path", request.getServletPath());
            response.addHeader("WWW-Authenticate", "Bearer realm=\"ecohub\"");

            final ObjectMapper mapper = new ObjectMapper();
            try {
                mapper.writeValue(response.getOutputStream(), body);
            } catch (IOException e) {
                log.warn("Error while writing JWT problem status", e);
            }
        }
    }
}
