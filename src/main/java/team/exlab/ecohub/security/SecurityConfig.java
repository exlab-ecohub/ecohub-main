package team.exlab.ecohub.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
class SecurityConfig {

    private final KeycloakLogoutHandler keycloakLogoutHandler;
    private final JwtAuthConverter jwtAuthConverter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests()
                .antMatchers("/admin/**")
                .hasAnyRole("admin", "superadmin")
                .antMatchers("/superadmin/**")
                .hasRole("superadmin")
                .anyRequest()
                .permitAll();
        http.oauth2Login()
                .and()
                .logout()
                .addLogoutHandler(keycloakLogoutHandler)
                .logoutSuccessUrl("/");
        http.oauth2ResourceServer().jwt()
                .jwtAuthenticationConverter(jwtAuthConverter);
        return http.build();
    }
}
