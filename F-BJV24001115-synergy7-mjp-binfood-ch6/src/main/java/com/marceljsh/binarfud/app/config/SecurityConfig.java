package com.marceljsh.binarfud.app.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marceljsh.binarfud.auth.service.AuthService;
import com.marceljsh.binarfud.common.dto.ErrorResponse;
import com.marceljsh.binarfud.security.filter.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

  private static final String[] SWAGGER_WHITELIST = {
      "/swagger-ui/**",
      "/v3/api-docs/**",
      "/swagger-resources/**",
      "/swagger-resources"
  };

  private static final String[] AUTH_WHITELIST = {
      "/api/v1/auth/sign-up",
      "/api/v1/auth/sign-in"
  };

  private final JwtAuthenticationFilter jwtAuthFilter;

  private final AuthenticationProvider authProvider;

  private final ObjectMapper objectMapper;

  private final OidcUserService oidcUserService;

  private final AuthService authService;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(AbstractHttpConfigurer::disable)
        .cors(withDefaults())
        .authenticationProvider(authProvider)
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        .formLogin(withDefaults())

        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

        .authorizeHttpRequests(auth -> auth
            .requestMatchers(HttpMethod.POST, AUTH_WHITELIST).permitAll()
            .requestMatchers("/login").permitAll()
            .requestMatchers(SWAGGER_WHITELIST).permitAll()
            .anyRequest().authenticated())

        .oauth2Login(oauth2 -> oauth2
            .userInfoEndpoint(userInfo -> userInfo
                .oidcUserService(oidcUserService))
            .successHandler((request, response, authentication) -> {
              DefaultOidcUser oidcUser = (DefaultOidcUser) authentication.getPrincipal();
              authService.registerOAuth2(oidcUser.getAttribute("email"));

              response.sendRedirect("/auth/oauth2/success");
            }))

//        .exceptionHandling(exceptions -> exceptions
//            .authenticationEntryPoint(this::handleAuthenticationException)
//            .accessDeniedHandler(this::handleAccessDeniedException))
    ;

    return http.build();
  }

  private void handleAuthenticationException(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException) throws IOException {
    sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "Unauthorized " + authException.getMessage());
  }

  private void handleAccessDeniedException(HttpServletRequest request, HttpServletResponse response,
      AccessDeniedException accessDeniedException) throws IOException {
    sendErrorResponse(response, HttpStatus.FORBIDDEN, "Access denied " + accessDeniedException.getMessage());
  }

  private void sendErrorResponse(HttpServletResponse response, HttpStatus status, String message) throws IOException {
    response.setStatus(status.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    ErrorResponse errorResponse = ErrorResponse.from(message);
    objectMapper.writeValue(response.getWriter(), errorResponse);
  }

}
