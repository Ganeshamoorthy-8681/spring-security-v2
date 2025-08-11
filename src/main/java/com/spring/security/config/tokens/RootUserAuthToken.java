package com.spring.security.config.tokens;

import java.util.Collection;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

/**
 * Represents an authentication token for a root user. This token is used to authenticate root users
 * in the application. It extends UsernamePasswordAuthenticationToken to provide the necessary
 * authentication details.
 */
public class RootUserAuthToken extends UsernamePasswordAuthenticationToken {

  /**
   * Constructor for RootUserAuthToken.
   *
   * @param email the email of the root user
   * @param password the password of the root user
   */
  public RootUserAuthToken(String email, String password) {
    super(email, password);
  }

  /**
   * Constructor for RootUserAuthToken with authorities.
   *
   * @param principal the principal (email) of the root user
   * @param credentials the credentials (password) of the root user
   * @param authorities the authorities granted to the root user
   */
  public RootUserAuthToken(
      Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
    super(principal, credentials, authorities);
  }
}
