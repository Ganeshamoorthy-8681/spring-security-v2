package com.spring.security.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

/**
 * Standardized error response format for API exceptions.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseDto {

    /** The error message describing what went wrong */
    private String message;

    /** HTTP status code */
    private int code;

    /** List of nested exception causes */
    private List<String> causes;

    /** Timestamp when the error occurred */
    private Instant timestamp;

    /** Request path where the error occurred */
    private String path;

    public ErrorResponseDto(String message, int code, List<String> causes) {
        this.message = message;
        this.code = code;
        this.causes = causes;
        this.timestamp = Instant.now();
    }
}
