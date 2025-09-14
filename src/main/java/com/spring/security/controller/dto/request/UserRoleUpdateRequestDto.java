package com.spring.security.controller.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Request DTO for updating user roles.
 */
@Getter
@Setter
public class UserRoleUpdateRequestDto {

  /** The list of role IDs to assign to the user. */
  @NotNull(message = "Role IDs are required")
  @NotEmpty(message = "At least one role must be specified")
  private List<Long> roleIds;
}
