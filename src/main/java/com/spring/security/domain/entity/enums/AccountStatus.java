package com.spring.security.domain.entity.enums;

import lombok.Getter;

/**
 * Enum representing the status of an account.
 *
 * <p>This enum defines the following statuses:
 *
 * <ul>
 *   <li>CREATED - Indicates that the account has been created but not yet verified.
 *   <li>ACTIVE - Indicates that the account's email has been verified and is active.
 *   <li>SUSPENDED - Indicates that the account has been temporarily disabled.
 *   <li>DELETED - Indicates that the account has been permanently removed.
 * </ul>
 */
@Getter
public enum AccountStatus {

  /** Indicates that the account has been created but not yet verified. */
  CREATED,

  /** Indicates that the account's email has been verified. */
  ACTIVE,

  /** Indicates that the account has been temporarily disabled. */
  SUSPENDED,

  /** Indicates that the account has been permanently removed. */
  DELETED;
}
