package com.spring.security.exceptions;

/**
 * Exception class for handling errors in the Data Access Object (DAO) layer. This exception is
 * thrown when there are issues related to data access operations.
 */
public class DaoLayerException extends BaseException {

  /**
   * Default constructor for DaoLayerException. Initializes the exception with a default message.
   */
  public DaoLayerException() {
    super("An error occurred in the DAO layer.");
  }

  /**
   * Constructor for DaoLayerException with a custom message.
   *
   * @param message the detail message for the exception
   */
  public DaoLayerException(String message) {
    super(message);
  }

  public DaoLayerException(String message, Throwable cause) {
    super(message, cause);
  }
}
