package com.spring.security.exceptions;

/**
 * Exception class to handle email service related exceptions.
 *
 * <p>This exception is thrown when there are issues with the email service, such as sending emails,
 * connecting to the email server, etc.
 */
public class EmailServiceException extends BaseException {

  public EmailServiceException(String message) {
    super(message);
  }

  public EmailServiceException(String message, Throwable cause) {
    super(message, cause);
  }
}
