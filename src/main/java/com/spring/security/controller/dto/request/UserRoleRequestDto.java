package com.spring.security.controller.dto.request;

import lombok.Getter;
import lombok.Setter;

/** DTO used for setting user role details in requests. */
@Getter
@Setter
public class UserRoleRequestDto {

  /** The id of the role. */
  private Long id;
}
