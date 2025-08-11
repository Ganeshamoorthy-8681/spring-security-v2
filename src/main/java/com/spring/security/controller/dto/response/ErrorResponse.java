package com.spring.security.controller.dto.response;

import lombok.Getter;
import lombok.Setter;

/**
 * ErrorResponse is a DTO class that represents the structure of an error response.
 * It contains fields for the error message, code, and additional details.
 */
@Getter
@Setter
public class ErrorResponse {

    private String message;

    private String code;

    private String details;
}
