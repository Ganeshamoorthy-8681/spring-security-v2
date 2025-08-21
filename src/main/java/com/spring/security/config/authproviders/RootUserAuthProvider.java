package com.spring.security.config.authproviders;

import static com.spring.security.util.AuthUtil.getAuthorities;

import com.spring.security.config.tokens.RootUserAuthToken;
import com.spring.security.dao.UserDao;
import com.spring.security.domain.entity.CustomUserDetails;
import com.spring.security.domain.entity.User;
import com.spring.security.exceptions.DaoLayerException;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * This class implements the AuthenticationProvider interface to provide custom authentication
 * logic. It is designed to handle authentication for root users.
 */
@Component
@Slf4j
public class RootUserAuthProvider implements AuthenticationProvider {

  private final UserDao userDao;

  private final PasswordEncoder passwordEncoder;

  /**
   * Constructor to initialize the RootUserAuthProvider with UserDao and PasswordEncoder.
   *
   * @param userDao the UserDao instance for user data access.
   * @param passwordEncoder the PasswordEncoder instance for password encoding.
   */
  public RootUserAuthProvider(UserDao userDao, PasswordEncoder passwordEncoder) {
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
   * @throws AuthenticationException if authentication fails due to invalid credentials or other
   *     issues
   */
  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {

    RootUserAuthToken token = (RootUserAuthToken) authentication;
    String email = token.getName();
    String password =
        Optional.ofNullable(token.getCredentials()).map(Object::toString).orElse(null);

    User user = loadUserByEmail(email);
    validatePassword(user, password);

    UserDetails userDetails =
        new CustomUserDetails(user.getEmail(), user.getPassword(), getAuthorities(user.getRoles()));

    return new RootUserAuthToken(userDetails, user.getPassword(), userDetails.getAuthorities());
  }

  /**
   * Loads a user by their email address.
   *
   * @param email the email address of the user to load
   * @return the User object if found
   * @throws UsernameNotFoundException if no user is found with the given email
   * @throws InternalAuthenticationServiceException if there is an error retrieving the user
   */
  private User loadUserByEmail(String email) {
    try {
      User user = userDao.findByEmail(email);
      if (user == null) {
        log.error("User not found with email: {}", email);
        throw new UsernameNotFoundException("User not found with email: " + email);
      }
      if (!isRootUser(user)) {
        log.error("User with email {} is not a root user", email);
        throw new BadCredentialsException(
            "User is not a root user, try with account user credentials");
      }
      return user;
    } catch (DaoLayerException e) {
      log.error("Error retrieving user by email {}: {}", email, e.getMessage());
      throw new InternalAuthenticationServiceException("Internal error retrieving user", e);
    }
  }

  /**
   * Validates the provided password against the stored password for the user.
   *
   * @param user the User object containing the stored password
   * @param rawPassword the raw password to validate
   * @throws BadCredentialsException if the raw password does not match the stored password
   */
  private void validatePassword(User user, String rawPassword) {
    log.warn("Validating password for user: {}", user.getEmail());
    if (rawPassword != null && !passwordEncoder.matches(rawPassword, user.getPassword())) {
      throw new BadCredentialsException("Invalid credentials");
    }
  }

  /**
   * Checks if the user is a root user.
   *
   * @param user the user to check
   * @return true if the user is a root user, false otherwise
   */
  private boolean isRootUser(User user) {
    return user.getRoles().stream().anyMatch(role -> role.getName().equalsIgnoreCase("ROOT"));
  }

  /**
   * Determines whether this AuthenticationProvider supports the given Authentication object.
   *
   * @param authentication the class of the authentication object
   * @return true if the implementation can evaluate the provided Authentication object
   */
  @Override
  public boolean supports(Class<?> authentication) {
    return RootUserAuthToken.class.isAssignableFrom(authentication);
  }
}
