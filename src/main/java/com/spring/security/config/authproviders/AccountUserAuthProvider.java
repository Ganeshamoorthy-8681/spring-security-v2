package com.spring.security.config.authproviders;

import com.spring.security.config.tokens.AccountUserAuthToken;
import com.spring.security.dao.UserDao;
import com.spring.security.domain.entity.CustomUserDetails;
import com.spring.security.domain.entity.User;
import com.spring.security.exceptions.AuthenticationException;
import com.spring.security.exceptions.DaoLayerException;
import com.spring.security.util.AuthUtil;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Custom AuthenticationProvider implementation for authenticating users based on account-specific
 * credentials. This class integrates with Spring Security and provides authentication logic
 * tailored to the application's requirements.
 */
@Slf4j
@Component
public class AccountUserAuthProvider implements AuthenticationProvider {

  private final UserDao userDao;

  private final PasswordEncoder passwordEncoder;

  /**
   * Constructor for AccountAuthProvider.
   *
   * @param userDao the UserDao to be used for database operations
   * @param passwordEncoder the PasswordEncoder to encode and verify passwords
   */
  public AccountUserAuthProvider(UserDao userDao, PasswordEncoder passwordEncoder) {
    this.userDao = userDao;
    this.passwordEncoder = passwordEncoder;
  }

  /**
   * Performs authentication with the same contract as {@link
   * AuthenticationManager#authenticate(Authentication)}.
   *
   * @param authentication the authentication request object containing credentials and account
   *     details
   * @return a fully authenticated object including credentials and authorities
   */
  @Override
  public Authentication authenticate(Authentication authentication) {
    try {
      AccountUserAuthToken token = (AccountUserAuthToken) authentication;
      String email = token.getName();
      String password =
          Optional.ofNullable(token.getCredentials()).map(Object::toString).orElse(null);
      Long accountId = token.getAccountId();
      log.info("Authenticating user: {} for account: {}", email, accountId);
      User user = fetchUserByAccountAndEmail(accountId, email);
      validatePassword(password, user.getPassword());
      UserDetails userDetails = buildUserDetails(user);

      return new AccountUserAuthToken(
          userDetails, user.getPassword(), accountId, userDetails.getAuthorities());

    } catch (UsernameNotFoundException | BadCredentialsException e) {
      log.warn("Authentication failed: {}", e.getMessage());
      throw e;
    } catch (Exception e) {
      log.error("Unexpected error during authentication", e);
      throw new AuthenticationException("Internal error occurred during authentication", e);
    }
  }

  /**
   * Fetches a user by account ID and email.
   *
   * @param accountId the ID of the account
   * @param email the email of the user
   * @return the User object if found
   * @throws DaoLayerException if there is an error accessing the database
   */
  private User fetchUserByAccountAndEmail(Long accountId, String email) throws DaoLayerException {
    User user = userDao.findByAccountIdAndEmail(accountId, email);
    if (user == null) {
      throw new UsernameNotFoundException("User not found for email: " + email);
    }
    return user;
  }

  /**
   * Validates the provided raw password against the encoded password.
   *
   * @param rawPassword the raw password to validate
   * @param encodedPassword the encoded password to compare against
   * @throws BadCredentialsException if the passwords do not match
   */
  private void validatePassword(String rawPassword, String encodedPassword) {
    if (rawPassword != null && !passwordEncoder.matches(rawPassword, encodedPassword)) {
      throw new BadCredentialsException("Invalid password");
    }
  }

  /**
   * Builds a UserDetails object from the User entity.
   *
   * @param user the User entity
   * @return a UserDetails object containing user information and authorities
   */
  private UserDetails buildUserDetails(User user) {
    return new CustomUserDetails(
        user.getAccountId(),
        user.getEmail(),
        user.getPassword(),
        AuthUtil.getAuthorities(user.getRoles()));
  }

  /**
   * Determines whether this AuthenticationProvider supports the given Authentication object.
   *
   * @param authentication the class of the authentication object
   * @return true if the implementation can evaluate the provided Authentication object
   */
  @Override
  public boolean supports(Class<?> authentication) {
    return AccountUserAuthToken.class.isAssignableFrom(authentication);
  }
}
