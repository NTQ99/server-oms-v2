package ntq.server.oms.config;

import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import ntq.server.common.controller.ExceptionController;
import ntq.server.common.security.JwtAuthenticationFilter;
import ntq.server.common.security.KeycloakJwtAuthenticator;
import ntq.server.common.thirdparty.keycloak.KeycloakClient;
import ntq.server.common.thirdparty.keycloak.impl.KeycloakClientImpl;
import ntq.server.common.util.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class WebSecurityConfiguration {
  private final ExceptionController exceptionController;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationFilter authenticationFilter) throws Exception {
    http.cors(corsConfigurer -> corsConfigurer.configurationSource(corsConfigurationSource()))
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    http.authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests.dispatcherTypeMatchers(DispatcherType.ASYNC).permitAll()
        .requestMatchers(HttpMethod.GET, "/actuator/**").permitAll()
        .anyRequest().authenticated());
    http.exceptionHandling(exceptionHandling ->
        exceptionHandling.accessDeniedHandler((request, response, accessDeniedException) -> exceptionController.handleAccessDeniedException(accessDeniedException, request, response))
            .authenticationEntryPoint((request, response, authException) -> exceptionController.handleAuthenticationException(authException, request, response)));
    http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }

  @Bean
  JwtAuthenticationFilter authenticationFilter(KeycloakClient keycloakClient) {
    return new JwtAuthenticationFilter(new KeycloakJwtAuthenticator(keycloakClient));
  }

  @Bean
  KeycloakClient keycloakClient(RestClient restClient, @Value("${authentication.keycloak.jwks-url}") String jwksUrl) {
    return new KeycloakClientImpl(restClient, jwksUrl);
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    var configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(List.of("*"));
    configuration.setAllowedMethods(List.of("*"));
    configuration.setAllowedHeaders(List.of("*"));
    var source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}
