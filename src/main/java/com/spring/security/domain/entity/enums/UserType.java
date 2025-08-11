package com.spring.security.domain.entity.enums;

/**
 * Enum representing the type of user.
 *
 * <p>This enum defines the following user types:
 *
 * <ul>
 *   <li>SSO - Represents a Single Sign-On user.
 *   <li>PASSWORD - Represents a password-based user.
 * </ul>
 */
public enum UserType {

  /** Represents a Single Sign-On user. */
  SSO,

  /** Represents a password-based user. */
  PASSWORD,
}
