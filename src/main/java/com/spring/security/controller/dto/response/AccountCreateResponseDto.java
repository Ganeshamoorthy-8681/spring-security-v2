package com.spring.security.controller.dto.response;

import com.spring.security.domain.entity.enums.AccountStatus;
import com.spring.security.domain.entity.enums.AccountType;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) for account creation response.
 *
 * <p>This DTO encapsulates the details of an account that has been created, including its unique
 * identifier, name, description, type, status, associated user ID, and email.
 */
@Getter
@Setter
public class AccountCreateResponseDto {

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

  private Long userId;

  private String email;
}
