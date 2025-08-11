package com.spring.security.exceptions;

/**
 * BaseException is the base class for all custom exceptions in the application.
 * It extends the Exception class and provides a constructor to set the exception message.
 */
public class BaseException extends Exception{

    /**
     * Default constructor for BaseException that sets a default message.
     */
    public BaseException(){
        super("Something went wrong");
    }

    /**
     * Constructor to create a BaseException with a specific message.
     *
     * @param message the detail message for the exception
     */
    public BaseException(String message) {
        super(message);
    }

    /**
     * Constructor to create a BaseException with a specific message and cause.
     *
     * @param message the detail message for the exception
     * @param cause the cause of the exception
     */
    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

}
