package com.spring.security.controller.dto.request;

import lombok.Getter;
import lombok.Setter;

/** DTO for requesting a user by email. */
@Getter
@Setter
public class GetUserByEmailRequestDto {

  private String email;
}
