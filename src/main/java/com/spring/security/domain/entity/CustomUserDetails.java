package com.spring.security.domain.entity;

import java.util.Collection;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/** UserDetails implementation that represents a custom user in the application. */
@Getter
@Setter
public class CustomUserDetails implements UserDetails {

  private String email;

  private Long accountId;

  private String password;

  private List<SimpleGrantedAuthority> authorities;

  /**
   * Constructor for creating a CustomUserDetails object.
   *
   * @param email the email of the user
   * @param password the password of the user
   * @param authorities the list of authorities granted to the user
   */
  public CustomUserDetails(
      String email, String password, List<SimpleGrantedAuthority> authorities) {
    this.email = email;
    this.password = password;
    this.authorities = authorities;
  }

  /**
   * Constructor for creating a CustomUserDetails object with an account ID.
   *
   * @param accountId the ID of the account
   * @param email the email of the user
   * @param password the password of the user
   * @param authorities the list of authorities granted to the user
   */
  public CustomUserDetails(
      Long accountId, String email, String password, List<SimpleGrantedAuthority> authorities) {
    this.accountId = accountId;
    this.email = email;
    this.password = password;
    this.authorities = authorities;
  }

  /**
   * Returns the authorities granted to the user. Cannot return <code>null</code>.
   *
   * @return the authorities, sorted by natural key (never <code>null</code>)
   */
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  /**
   * Returns the password used to authenticate the user.
   *
   * @return the password
   */
  @Override
  public String getPassword() {
    return password;
  }

  /**
   * Returns the username used to authenticate the user. Cannot return <code>null</code>.
   *
   * @return the username (never <code>null</code>)
   */
  @Override
  public String getUsername() {
    return email;
  }

  /**
   * Indicates whether the user's account has expired. An expired account cannot be authenticated.
   *
   * @return <code>true</code> if the user's account is valid (ie non-expired), <code>false</code>
   *     if no longer valid (ie expired)
   */
  @Override
  public boolean isAccountNonExpired() {
    return UserDetails.super.isAccountNonExpired();
  }

  /**
   * Indicates whether the user is locked or unlocked. A locked user cannot be authenticated.
   *
   * @return <code>true</code> if the user is not locked, <code>false</code> otherwise
   */
  @Override
  public boolean isAccountNonLocked() {
    return UserDetails.super.isAccountNonLocked();
  }

  /**
   * Indicates whether the user's credentials (password) has expired. Expired credentials prevent
   * authentication.
   *
   * @return <code>true</code> if the user's credentials are valid (ie non-expired), <code>false
   *     </code> if no longer valid (ie expired)
   */
  @Override
  public boolean isCredentialsNonExpired() {
    return UserDetails.super.isCredentialsNonExpired();
  }

  /**
   * Indicates whether the user is enabled or disabled. A disabled user cannot be authenticated.
   *
   * @return <code>true</code> if the user is enabled, <code>false</code> otherwise
   */
  @Override
  public boolean isEnabled() {
    return UserDetails.super.isEnabled();
  }
}
