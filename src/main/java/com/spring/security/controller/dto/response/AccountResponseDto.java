package com.spring.security.controller.dto.response;

import com.spring.security.domain.entity.enums.AccountStatus;
import com.spring.security.domain.entity.enums.AccountType;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a response DTO for account details.
 *
 * <p>This class contains the following fields:
 *
 * <ul>
 *   <li>id - The unique identifier of the account.
 *   <li>name - The name of the account holder.
 *   <li>description - A brief description of the account holder.
 *   <li>type - The type of the account, represented by the {@link AccountType} enum.
 *   <li>status - The current status of the account, represented by the {@link AccountStatus} enum.
 *   <li>additionalAttributes - A map containing additional attributes associated with the account.
 * </ul>
 */
@Getter
@Setter
public class AccountResponseDto {

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
}
