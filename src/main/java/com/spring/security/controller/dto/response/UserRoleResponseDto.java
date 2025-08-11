package com.spring.security.controller.dto.response;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

/** Dto representing a user role response. */
@Getter
@Setter
public class UserRoleResponseDto {

  /** The unique identifier of the role. */
  private Long id;

  /** The name of the role. */
  private String name;

  /** A brief description of the role. */
  private String description;

  /** The permissions associated with the role. */
  private List<PermissionResponseDto> permissions;
}
