package com.marceljsh.binarfud.app.config;

import com.marceljsh.binarfud.security.filter.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private static final String[] SWAGGER_WHITELIST = {
      "/swagger-ui/**",
      "/v3/api-docs/**",
      "/swagger-resources/**",
      "/swagger-resources"
  };

  private final JwtAuthenticationFilter jwtAuthFilter;

  private final AuthenticationProvider authProvider;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(
                "/api/v1/auth/sign-up",
                "/api/v1/auth/sign-in",
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/swagger-resources/**",
                "/swagger-resources")
            .permitAll()
            .anyRequest().authenticated())
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authenticationProvider(authProvider)
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        .exceptionHandling(exceptions -> exceptions
            .authenticationEntryPoint((request, response, e) -> response.sendError(
                HttpServletResponse.SC_UNAUTHORIZED,
                e.getMessage())));

    return http.build();
  }

}
