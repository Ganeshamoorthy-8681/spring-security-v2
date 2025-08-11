package com.spring.security.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;


/**
 * The `AuthEntryPoint` class is a custom implementation of the `AuthenticationEntryPoint` interface.
 * It is used to handle unauthorized access attempts in a Spring Security application.
 * When an unauthenticated user tries to access a protected resource, this class is invoked to send an appropriate response.
 */
@Component
public class AuthEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    /**
     * Handles authentication exceptions by resolving them through the HandlerExceptionResolver.
     *
     * @param request   the HttpServletRequest that resulted in an AuthenticationException
     * @param response  the HttpServletResponse to send the error response
     * @param exception the AuthenticationException that was thrown
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) {
        resolver.resolveException(request, response, null, exception);
    }
}
