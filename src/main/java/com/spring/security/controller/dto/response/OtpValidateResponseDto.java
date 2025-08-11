package com.spring.security.controller.dto.response;

import lombok.Getter;
import lombok.Setter;

/** Validates the OTP response. */
@Getter
@Setter
public class OtpValidateResponseDto {

  /** The status of the OTP verification. */
  private OtpValidationStatus status;
}
