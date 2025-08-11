package com.spring.security.controller.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PermissionResponseDto {

  /** Unique identifier for the permission. This is typically a database-generated ID. */
  private Long id;

  /** The name of the permission. */
  private String name;

  /** A description of what the permission allows. */
  private String description;
}
