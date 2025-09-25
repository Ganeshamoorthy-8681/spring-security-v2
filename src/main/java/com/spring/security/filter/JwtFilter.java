package com.spring.security.filter;

import com.spring.security.component.JwtTokenGenerator;
import com.spring.security.config.tokens.AccountUserAuthToken;
import com.spring.security.config.tokens.RootUserAuthToken;
import com.spring.security.exceptions.AuthenticationException;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

/** This filter used to authenticate JWT tokens in the request. */
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

  private final JwtTokenGenerator jwtTokenGenerator;

  private final AuthenticationManager authenticationManager;

  private final AntPathMatcher pathMatcher = new AntPathMatcher();

  private static final List<String> EXCLUDED_PATHS =
      List.of(
          "/api/v1/auth/**",
          "/api/v1/accounts/*/users/set-password",
          "/api/v1/accounts/*/users/forgot-password",
          "/api/v1/accounts/create",
          "/api/v1/otp/**");

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    String path = request.getServletPath();
    return EXCLUDED_PATHS.stream().anyMatch(pattern -> pathMatcher.match(pattern, path));
  }

  public JwtFilter(
      JwtTokenGenerator jwtTokenGenerator, AuthenticationManager authenticationManager) {
    this.jwtTokenGenerator = jwtTokenGenerator;
    this.authenticationManager = authenticationManager;
  }

  /**
   * This method is called for every request to filter the JWT token.
   *
   * @param request the HTTP request
   * @param response the HTTP response
   * @param filterChain the filter chain to continue the request processing
   * @throws ServletException thrown if an error occurs during request processing
   * @throws IOException thrown if an I/O error occurs during request processing
   */
  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    log.info("requestURI={}", request.getRequestURI());

    String authHeader = request.getHeader("Authorization");

    if (authHeader != null && authHeader.startsWith("Bearer")) {
      String jwt = authHeader.replace("Bearer", "").trim(); // remove "Bearer "

      try {
        Claims claims = jwtTokenGenerator.getClaims(jwt);
        String email = (String) claims.get("email");
        boolean isRoot = (boolean) claims.get("isRoot");

        Long accountId =
            Optional.ofNullable(claims.get("accountId"))
                .filter(Number.class::isInstance)
                .map(Number.class::cast)
                .map(Number::longValue)
                .orElse(null);

        log.info(
            "JWT Token Claims: username={}, isRoot={}, accountId={}", email, isRoot, accountId);

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

          Authentication authToken;

          if (isRoot) {
            authToken = new RootUserAuthToken(email, null);
          } else {
            authToken = new AccountUserAuthToken(accountId, email, null);
          }

          // Authenticate the token using the authentication manager
          Authentication authenticated = authenticationManager.authenticate(authToken);
          SecurityContextHolder.getContext().setAuthentication(authenticated);
        }

      } catch (Exception e) {
        throw new AuthenticationException("Failed to Validate", e);
      }
    }

    filterChain.doFilter(request, response);
  }
}
