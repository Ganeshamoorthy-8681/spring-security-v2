package com.spring.security.exceptions;

/**
 * Thrown when the input parameters are null or empty.
 *
 * <p>The failure scenarios include:
 *
 * <ul>
 *   <li>when the input parameter is null.
 *   <li>when the input parameter is empty.
 * </ul>
 */
public class PreconditionViolationException extends BaseException {

  /**
   * Constructor to create {@link PreconditionViolationException}.
   *
   * @param message the detail message for the exception
   */
  public PreconditionViolationException(String message) {
    super(message);
  }
}
