package com.spring.security.controller.dto.request;

import com.spring.security.domain.entity.enums.AccountType;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for creating a new account.
 *
 * <p>This class is used to encapsulate the data required to create a new account, including the
 * account's name, description, and type.
 */
@Getter
@Setter
public class AccountCreateRequestDto {

  /** The name of the account holder. */
  private String name;

  /** A brief description of the account holder. */
  private String description;

  /** The type of the account, represented by the {@link AccountType} enum. */
  private AccountType type;
}
