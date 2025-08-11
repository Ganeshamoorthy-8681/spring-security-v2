package com.spring.security.controller.dto.request;

import lombok.Getter;
import lombok.Setter;

/** DTO for user login requests. */
@Getter
@Setter
public class LoginRequestDto {

  private Long accountId;

  private String email;

  private String password;
}
