package com.spring.security.exceptions;

/**
 * Exception thrown when JWT token generation fails.
 *
 * <p>This exception indicates that the system encountered an error while trying to generate a JWT
 * token, which is typically used for authentication and authorization purposes.
 */
public class JwtTokenGenerationFailedException extends BaseException {

  /**
   * Constructs a new JwtTokenGenerationFailedException with the specified detail message.
   *
   * @param message the detail message
   */
  public JwtTokenGenerationFailedException(String message) {
    super(message);
  }

  /**
   * Constructs a new JwtTokenGenerationFailedException with the specified detail message and cause.
   *
   * @param message the detail message
   * @param cause the cause of the exception
   */
  public JwtTokenGenerationFailedException(String message, Throwable cause) {
    super(message, cause);
  }
}
