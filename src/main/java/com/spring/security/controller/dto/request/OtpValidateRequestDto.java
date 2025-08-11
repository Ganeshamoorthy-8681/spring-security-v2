package com.spring.security.controller.dto.request;

import lombok.Getter;
import lombok.Setter;

/** DTO for validating an otp of an email address. */
@Getter
@Setter
public class OtpValidateRequestDto {

  private String email;

  private int otp;

  private Long accountId;
}
