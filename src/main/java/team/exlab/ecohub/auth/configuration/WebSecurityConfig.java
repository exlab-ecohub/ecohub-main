package team.exlab.ecohub.auth.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import team.exlab.ecohub.auth.configuration.jwt.AuthTokenFilter;
import team.exlab.ecohub.auth.configuration.jwt.JwtExceptionFilter;
import team.exlab.ecohub.auth.configuration.jwt.JwtService;
import team.exlab.ecohub.auth.configuration.jwt.UnauthorizedEntryPointJwt;
import team.exlab.ecohub.user.service.UserServiceImpl;

@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final UnauthorizedEntryPointJwt unauthorizedHandler;
    private final LogoutHandler logoutHandler;
    private final ApplicationContext context;

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder, UserDetailsService userDetailsService)
            throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder)
                .and()
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //enables CORS
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedMethods("PUT", "DELETE",
                        "GET", "POST", "PATCH");
            }
        };
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    protected SecurityFilterChain defaultSecurity(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .requiresChannel(channel ->
                        channel.anyRequest().requiresSecure())
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .requestMatchers()
                .antMatchers("/auth/**")
                .antMatchers("/recycling-points/**")
                .antMatchers("/feedbacks/**")
                .antMatchers("/swagger/**")
                .antMatchers("/swagger-ui/**")
                .antMatchers("/swagger-download/**");
        http.logout()
                .logoutUrl("/auth/logout")
                .addLogoutHandler(logoutHandler)
                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext());
        return http.build();
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .requiresChannel(channel ->
                        channel.anyRequest().requiresSecure())
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeHttpRequests()
                .antMatchers("/admin/**")
                .hasAnyRole("ADMIN", "SUPERADMIN")
                .antMatchers("/superadmin/**")
                .hasRole("SUPERADMIN")
                .antMatchers("/user/**")
                .hasRole("USER");
        http.addFilterBefore(new AuthTokenFilter(context.getBean(JwtService.class), context.getBean(UserServiceImpl.class)),
                UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(new JwtExceptionFilter(), AuthTokenFilter.class);
        http.logout()
                .logoutUrl("/auth/logout")
                .addLogoutHandler(logoutHandler)
                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext());
        return http.build();
    }
}
