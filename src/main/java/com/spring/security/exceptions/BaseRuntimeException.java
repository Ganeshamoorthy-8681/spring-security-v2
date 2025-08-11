package com.spring.security.exceptions;

public class BaseRuntimeException extends RuntimeException{

    BaseRuntimeException(String message) {
        super(message);
    }

    BaseRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
