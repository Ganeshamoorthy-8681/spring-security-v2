package com.spring.security.exceptions;

/**
 * ResourceAlreadyExistException is thrown when an attempt is made to create a resource that already
 * exists. This exception extends ServiceLayerException to indicate an error in the service layer.
 */
public class ResourceAlreadyExistException extends ServiceLayerException {

  /**
   * Constructs a new ResourceAlreadyExistException with the specified detail message.
   *
   * @param message the detail message
   */
  public ResourceAlreadyExistException(String message) {
    super(message);
  }
}
