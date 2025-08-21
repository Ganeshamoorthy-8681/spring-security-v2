package com.spring.security.exceptions;

/**
 * Exception thrown when there is an error parsing a JWT token.
 *
 * <p>This exception is typically used to indicate that the JWT token is malformed or invalid.
 */
public class JwtTokenParseException extends BaseException {

  public JwtTokenParseException(String message) {
    super(message);
  }

  public JwtTokenParseException(String message, Throwable cause) {
    super(message, cause);
  }
}
