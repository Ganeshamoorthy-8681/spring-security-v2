package com.spring.security.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Request DTO for updating user profile and roles in a single operation.
 * Note: Email updates are not allowed for security reasons.
 */
@Getter
@Setter
public class UserUpdateRequestDto {

  /** The first name of the user. */
  @NotBlank(message = "First name is required")
  @Size(max = 50, message = "First name must not exceed 50 characters")
  private String firstName;

  /** The last name of the user. */
  @NotBlank(message = "Last name is required")
  @Size(max = 50, message = "Last name must not exceed 50 characters")
  private String lastName;

  /** The middle name of the user (optional). */
  @Size(max = 50, message = "Middle name must not exceed 50 characters")
  private String middleName;

  /** The list of role IDs to assign to the user (optional). */
  private List<Long> roleIds;
}
