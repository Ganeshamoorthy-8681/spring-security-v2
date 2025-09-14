package com.spring.security.domain.entity;

import com.spring.security.domain.entity.enums.AccountStatus;
import com.spring.security.domain.entity.enums.AccountType;
import java.time.Instant;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a Root account entity in the system.
 *
 * <p>This Root account is responsible for the
 *
 * <p>This class contains the following fields:
 *
 * <ul>
 *   <li>id - The unique identifier of the account.
 *   <li>name - The name of the account.
 *   <li>description - A brief description of the account.
 *   <li>type - The type of the account.
 *   <li>status - The current status of the account, represented by the {@link AccountStatus} enum.
 * </ul>
 */
@Getter
@Setter
public class Account {

  /** The unique identifier of the account. */
  private Long id;

  /** The name of the account holder. */
  private String name;

  /** The description of the account holder. */
  private String description;

  /** The type of the account. */
  private AccountType type;

  /** The current status of the account. */
  private AccountStatus status;

  /** Additional attributes associated with the account. */
  private Map<String, Object> additionalAttributes;

  /** The timestamp when the account was created. */
  private Instant createdAt;

  /** The timestamp when the account was last updated. */
  private Instant updatedAt;

  /** The user who created the account. */
  private String createdBy;

  /** The user who last updated the account. */
  private String updatedBy;
}
