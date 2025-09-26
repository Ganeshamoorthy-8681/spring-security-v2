package com.spring.security.controller.dto.request;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO for requesting a resend of the OTP (One Time Password). This class contains the email address
 * to which the OTP should be resent.
 */
@Getter
@Setter
public class OtpResendRequestDto {

  private String email;

  private Long accountId;

  private boolean isRoot;
}
