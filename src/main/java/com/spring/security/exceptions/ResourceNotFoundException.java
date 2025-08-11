package com.spring.security.exceptions;

/**
 * ResourceNotFoundException is an exception that is thrown when a requested resource is not found.1
 */
public class ResourceNotFoundException extends ServiceLayerException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

}
