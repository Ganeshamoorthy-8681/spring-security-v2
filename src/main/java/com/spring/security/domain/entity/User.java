package com.spring.security.domain.entity;

import com.spring.security.domain.entity.enums.UserStatus;
import com.spring.security.domain.entity.enums.UserType;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a user entity in the system.
 *
 * <p>This class contains the following fields:
 *
 * <ul>
 *   <li>id - The unique identifier of the user.
 *   <li>name - The name of the user.
 *   <li>email - The email address of the user.
 *   <li>password - The password for the user.
 *   <li>type - The type of the user, represented by the {@link UserType} enum.
 *   <li>accountId - The ID of the account associated with the user.
 *   <li>status - The current status of the user, represented by the {@link UserStatus} enum.
 *   <li>groups - A list of groups the user belongs to.
 *   <li>roles - A list of roles assigned to the user.
 *   <li>additionalAttributes - A map of additional attributes associated with the user.
 * </ul>
 */
@Getter
@Setter
public class User {

  /** The unique identifier of the user. */
  private Long id;

  /** The first name of the account holder. */
  private String firstName;

  /** The last name of the account holder. */
  private String lastName;

  /** The middle name of the account holder, if applicable. */
  private String middleName;

  /** The email address. */
  private String email;

  /** The password. */
  private String password;

  /** The type of the user, represented by the {@link UserType} enum. */
  private UserType type;

  /** The ID of the account associated with the user. */
  private Long accountId;

  /** The current status of the user, represented by the {@link UserStatus} enum. */
  private UserStatus status;

  /** The list of roles assigned to the user. */
  private List<Role> roles;

  /** The additional attributes associated with the user. */
  private Map<String, Object> additionalAttributes;

  /** The timestamp when the user was created. */
  private Instant createdAt;

  /** The timestamp when the user was last updated. */
  private Instant updatedAt;

  /** The timestamp when the user was deleted. */
  private Instant deletedAt;

  /** The timestamp of the user's current login. */
  private Instant currentLogin;

  /** The timestamp of the user's last login. */
  private Instant lastLogin;

  /** The number of failed login attempts. */
  private Integer failedLoginAttempts;

  /** The timestamp of the user's last failed login. */
  private Instant lastFailedLogin;

  /** The identifier of the user who created this record. */
  private String createdBy;

  /** The identifier of the user who last updated this record. */
  private String updatedBy;

  /** The identifier of the user who deleted this record. */
  private String deletedBy;

  private boolean isRoot;
}
