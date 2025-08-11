package com.spring.security.component;

import com.spring.security.exceptions.OtpGenerationFailedException;

public interface OtpGenerator {

  /**
   * Generates a one-time password (OTP) for the given email.
   *
   * @return the generated OTP as an integer
   */
  String generateOtp() throws OtpGenerationFailedException;
}
