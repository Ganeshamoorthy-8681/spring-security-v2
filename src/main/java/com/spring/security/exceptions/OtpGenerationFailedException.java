package com.spring.security.exceptions;

public class OtpGenerationFailedException extends BaseException {

  public OtpGenerationFailedException(String message) {
    super(message);
  }

  public OtpGenerationFailedException(String message, Throwable cause) {
    super(message, cause);
  }
}
