package com.spring.security.controller.dto.request;

import lombok.Getter;
import lombok.Setter;

/** DTO for setting a user's password. */
@Getter
@Setter
public class SetUserPasswordRequestDto {

  private String email;

  private String password;
}
