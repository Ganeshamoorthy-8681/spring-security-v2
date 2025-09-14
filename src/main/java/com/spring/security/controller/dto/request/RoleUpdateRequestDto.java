package com.spring.security.controller.dto.request;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

/** DTO for updating an existing role. */
@Getter
@Setter
public class RoleUpdateRequestDto {

  /** The name of the role. */
  private String name;

  /** The description of the role. */
  private String description;

  /** The permissions associated with the role. */
  private List<PermissionDto> permissions;
}
