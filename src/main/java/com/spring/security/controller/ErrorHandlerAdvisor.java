package com.spring.security.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for authentication and general exceptions.
 * TODO: Needs to Handle more specific exceptions and provide better error responses.
 */
@RestControllerAdvice
@Slf4j
public class ErrorHandlerAdvisor {

    @ExceptionHandler(AuthenticationException.class)
    public String handleAuthenticationException(AuthenticationException e) {
        log.debug("Authentication error:" );
        return "Authentication failed: " + e.getMessage();
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception e) {
        return "An error occurred: " + e.getMessage();
    }
}
