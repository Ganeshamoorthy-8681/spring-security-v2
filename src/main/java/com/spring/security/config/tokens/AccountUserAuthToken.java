package com.spring.security.config.tokens;

import java.util.Collection;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

/**
 * Custom authentication token for account users, extending the standard
 * UsernamePasswordAuthenticationToken. This token includes an account ID to associate the
 * authentication with a specific account based users.
 */
@Getter
@Setter
public class AccountUserAuthToken extends UsernamePasswordAuthenticationToken {

  private final Long accountId;

  /**
   * Constructs an AccountUserAuthToken with the specified account ID, email, and password.
   *
   * @param accountId the ID of the account associated with this authentication token
   * @param email the email of the user associated with this authentication token
   * @param password the password of the user associated with this authentication token
   */
  public AccountUserAuthToken(Long accountId, String email, String password) {
    super(email, password);
    this.accountId = accountId;
  }

  /**
   * Constructs an AccountUserAuthToken with the specified principal, credentials, account ID, and
   * authorities.
   *
   * @param principal the principal (user) associated with this authentication token
   * @param credentials the credentials (password) associated with this authentication token
   * @param accountId the ID of the account associated with this authentication token
   * @param authorities the collection of granted authorities for this authentication token
   */
  public AccountUserAuthToken(
      Object principal,
      Object credentials,
      Long accountId,
      Collection<? extends GrantedAuthority> authorities) {
    super(principal, credentials, authorities);
    this.accountId = accountId;
  }
}
