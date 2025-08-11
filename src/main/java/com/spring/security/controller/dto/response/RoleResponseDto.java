package com.spring.security.controller.dto.response;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) for representing a role response. This class encapsulates the details
 * of a role, including its ID, name, description, and associated account ID.
 */
@Getter
@Setter
public class RoleResponseDto {

  /** The unique identifier of the role. */
  private Long id;

  /** The name of the role. */
  private String name;

  /** A brief description of the role. */
  private String description;

  /** The ID of the account associated with the role. */
  private Long accountId;

  /** A list of permissions associated with the role. */
  private List<PermissionResponseDto> permissions;
}
