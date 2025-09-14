package com.spring.security.service;

import com.spring.security.annotation.LogActivity;
import com.spring.security.component.JwtTokenGenerator;
import com.spring.security.config.tokens.AccountUserAuthToken;
import com.spring.security.config.tokens.RootUserAuthToken;
import com.spring.security.controller.dto.request.LoginRequestDto;
import com.spring.security.controller.dto.request.RootLoginRequestDto;
import com.spring.security.domain.entity.CustomUserDetails;
import com.spring.security.exceptions.AuthenticationException;
import com.spring.security.exceptions.JwtTokenGenerationFailedException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

/**
 * LoginServiceImpl is a service class that implements the LoginService interface. It provides
 * methods to authenticate users based on their login credentials.
 */
@Service
@Slf4j
public class LoginServiceImpl implements LoginService {

  private final JwtTokenGenerator jwtTokenGenerator;
  private final AuthenticationManager authenticationManager;
  private final UserService userService;

  /**
   * Constructor for LoginServiceImpl.
   *
   * @param jwtTokenGenerator the JwtTokenGenerator used to generate JWT tokens
   * @param authenticationManager the AuthenticationManager for user authentication
   * @param userService the UserService for user operations
   */
  LoginServiceImpl(
      JwtTokenGenerator jwtTokenGenerator,
      AuthenticationManager authenticationManager,
      UserService userService) {
    this.jwtTokenGenerator = jwtTokenGenerator;
    this.authenticationManager = authenticationManager;
    this.userService = userService;
  }

  /**
   * Method to authenticate a user based on email and password.
   *
   * @param requestDto the login request containing email and password
   * @return a string representing the authentication result, typically a JWT token
   */
  @Override
  @LogActivity(action = "LOGIN", entityType = "USER", description = "Root user login attempt")
  public String authenticate(RootLoginRequestDto requestDto) throws AuthenticationException {
    try {
      RootUserAuthToken authenticationToken =
          new RootUserAuthToken(requestDto.getEmail(), requestDto.getPassword());
      String token = generateToken(authenticationToken);

      // Update login time for root user (accountId is null for root users)
      userService.updateLastLoginTime(null, requestDto.getEmail());
      return token;
    } catch (Exception e) {
      log.error("Authentication failed for user: {}", requestDto.getEmail(), e);
      throw new AuthenticationException("Failed to Authenticate", e);
    }
  }

  /**
   * Method to authenticate a user based on email, password, and account ID.
   *
   * @param requestDto the login request containing email, password, and account ID
   * @return a string representing the authentication result, typically a JWT token
   */
  @Override
  @LogActivity(action = "LOGIN", entityType = "USER", description = "Account user login attempt")
  public String authenticate(LoginRequestDto requestDto) throws AuthenticationException {
    try {
      AccountUserAuthToken authenticationToken =
          new AccountUserAuthToken(
              requestDto.getAccountId(), requestDto.getEmail(), requestDto.getPassword());
      String token = generateToken(authenticationToken);

      // Update login time for account user
      userService.updateLastLoginTime(requestDto.getAccountId(), requestDto.getEmail());
      return token;
    } catch (Exception e) {
      log.error("Authentication failed for user: {}", requestDto.getEmail(), e);
      throw new AuthenticationException("Failed to Authenticate", e);
    }
  }

  /**
   * Generates a JWT token for the authenticated user.
   * @param authenticationToken the authentication object containing user details
   * @return a string representing the generated JWT token
   */
  private String generateToken(Authentication authenticationToken)
      throws JwtTokenGenerationFailedException {

    Authentication authentication = authenticationManager.authenticate(authenticationToken);
    CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
    return jwtTokenGenerator.generate(prepareClaims(userDetails));
  }

  /**
   * Prepares the claims for the JWT token based on the user details.
   *
   * @param userDetails the user details containing information about the authenticated user
   * @return a map of claims to be included in the JWT token
   */
  private Map<String, Object> prepareClaims(CustomUserDetails userDetails) {

    boolean isRoot = isRootUser(userDetails.getAuthorities());
    Map<String, Object> claims =
        new HashMap<>(Map.of("isRoot", isRoot, "email", userDetails.getEmail()));
    if (userDetails.getAccountId() != null) {
      claims.put("accountId", userDetails.getAccountId());
    }
    return claims;
  }

  /**
   * Checks if the user has root authority.
   *
   * @param authorities the collection of granted authorities for the user
   * @return true if the user has root authority, false otherwise
   */
  private boolean isRootUser(Collection<? extends GrantedAuthority> authorities) {
    if (authorities == null || authorities.isEmpty()) {
      return false;
    }
    return authorities.stream().anyMatch(authority -> "ROLE_ROOT".equals(authority.getAuthority()));
  }
}
