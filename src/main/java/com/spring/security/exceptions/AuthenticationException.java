package com.spring.security.exceptions;

/** Exception class for handling authentication-related errors. */
public class AuthenticationException extends BaseRuntimeException {

  public AuthenticationException(String message) {
    super(message);
  }

  public AuthenticationException(String message, Throwable cause) {
    super(message, cause);
  }
}
