package com.spring.security.domain.entity.enums;

/**
 * Enum representing the status of a user.
 *
 * <p>This enum defines the following statuses:
 *
 * <ul>
 *   <li>ACTIVE - Indicates that the user is active and has access to the system.
 *   <li>INACTIVE - Indicates that the user is inactive and may not have access to the system.
 *   <li>DELETED - Indicates that the user has been removed from the system.
 * </ul>
 */
public enum UserStatus {

  /** Indicates that the user has been created but not yet activated. */
  CREATED,

  /** Indicates that the user is active and has access to the system. */
  ACTIVE,

  /** Indicates that the user is inactive and may not have access to the system. */
  INACTIVE,

  /** Indicates that the user has been removed from the system. */
  DELETED
}
