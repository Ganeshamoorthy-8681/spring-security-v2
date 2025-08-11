package com.spring.security.controller.dto.request;

import lombok.Getter;
import lombok.Setter;

/** Data Transfer Object (DTO) for representing a permission. */
@Getter
@Setter
public class PermissionDto {

  /** The unique identifier of the permission. */
  private Long id;

  /** The name of the permission. */
  private String name;

  /** A brief description of the permission. */
  private String description;
}
