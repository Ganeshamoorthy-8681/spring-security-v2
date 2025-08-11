package com.spring.security.exceptions;

/** * ServiceLayerException is an exception that is thrown when there is an error in the service layer of the application.
 * This could be due to various reasons such as business logic errors, data processing issues, etc.
 */
public class ServiceLayerException extends BaseException {

    public ServiceLayerException(String message) {
        super(message);
    }

    public ServiceLayerException(String message, BaseException cause) {
        super(message, cause);
    }
}
