package com.spring.security.controller.dto.request;

import lombok.Getter;
import lombok.Setter;

/** DTO for root login requests. */
@Getter
@Setter
public class RootLoginRequestDto {

  private String email;

  private String password;
}
