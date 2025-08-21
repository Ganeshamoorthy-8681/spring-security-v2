package com.spring.security.config;

import com.spring.security.component.JwtTokenGenerator;
import com.spring.security.config.authproviders.AccountUserAuthProvider;
import com.spring.security.config.authproviders.RootUserAuthProvider;
import com.spring.security.exceptions.AuthEntryPoint;
import com.spring.security.filter.JwtFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * SecurityConfig is a configuration class that sets up the security settings for the application.
 */
@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity()
public class SecurityConfig {

  @Autowired private AuthEntryPoint customAuthenticationEntryPoint;

  /**
   * AuthenticationManager bean is used to authenticate the user credentials. It uses the custom
   * authentication providers defined in the application.
   */
  @Bean
  public AuthenticationManager authenticationManager(
      AccountUserAuthProvider accountUserAuthProvider, RootUserAuthProvider rootUserAuthProvider) {
    return new ProviderManager(accountUserAuthProvider, rootUserAuthProvider);
  }

  /**
   * SecurityFilterChain bean is used to configure the security settings for the application. It
   * defines the authorization rules and session management policies.
   *
   * @param http HttpSecurity object to configure security settings
   * @return SecurityFilterChain object with configured security settings
   * @throws Exception if an error occurs during configuration
   */
  @Bean
  public SecurityFilterChain securityFilterChain(
      HttpSecurity http,
      AuthenticationManager authenticationManager,
      JwtTokenGenerator jwtTokenGenerator)
      throws Exception {
    JwtFilter jwtFilter = new JwtFilter(jwtTokenGenerator, authenticationManager);

    return http.csrf(AbstractHttpConfigurer::disable)
        .exceptionHandling(ex -> ex.authenticationEntryPoint(customAuthenticationEntryPoint))
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers(
                        "/api/v1/auth/**",
                        "/api/v1/accounts/*/users/root/create",
                        "/api/v1/accounts/*/users/set-password",
                        "/api/v1/accounts/*/users/forgot-password",
                        "/api/v1/accounts/create",
                        "/api/v1/otp/**")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .httpBasic(Customizer.withDefaults())
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
  }
}
