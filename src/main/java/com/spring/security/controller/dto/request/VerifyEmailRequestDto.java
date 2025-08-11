package com.spring.security.controller.dto.request;

import lombok.Getter;
import lombok.Setter;

/** DTO for validating an email address. */
@Getter
@Setter
public class VerifyEmailRequestDto {

  /** The email address to be verified. */
  public String email;
}
