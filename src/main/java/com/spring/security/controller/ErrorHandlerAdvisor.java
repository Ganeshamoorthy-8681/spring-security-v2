package com.spring.security.controller;

import com.spring.security.controller.dto.response.ErrorResponseDto;
import com.spring.security.exceptions.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Global exception handler for authentication and general exceptions.
 * Provides structured error responses with proper HTTP status codes and nested exception handling.
 */
@RestControllerAdvice
@Slf4j
public class ErrorHandlerAdvisor {

  // Custom Application Exceptions
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponseDto> handleResourceNotFoundException(
      ResourceNotFoundException e, HttpServletRequest request) {
    log.warn("Resource not found: {}", e.getMessage());
    ErrorResponseDto errorResponse = createErrorResponse(
        e.getMessage(), HttpStatus.NOT_FOUND.value(), extractCauses(e), request.getRequestURI());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
  }

  @ExceptionHandler(ResourceAlreadyExistException.class)
  public ResponseEntity<ErrorResponseDto> handleResourceAlreadyExistException(
      ResourceAlreadyExistException e, HttpServletRequest request) {
    log.warn("Resource already exists: {}", e.getMessage());
    ErrorResponseDto errorResponse = createErrorResponse(
        e.getMessage(), HttpStatus.CONFLICT.value(), extractCauses(e), request.getRequestURI());
    return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
  }

  @ExceptionHandler(PreconditionViolationException.class)
  public ResponseEntity<ErrorResponseDto> handlePreconditionViolationException(
      PreconditionViolationException e, HttpServletRequest request) {
    log.warn("Precondition violation: {}", e.getMessage());
    ErrorResponseDto errorResponse = createErrorResponse(
        e.getMessage(), HttpStatus.PRECONDITION_FAILED.value(), extractCauses(e), request.getRequestURI());
    return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(errorResponse);
  }

  @ExceptionHandler(ServiceLayerException.class)
  public ResponseEntity<ErrorResponseDto> handleServiceLayerException(
      ServiceLayerException e, HttpServletRequest request) {
    log.error("Service layer error: {}", e.getMessage(), e);
    ErrorResponseDto errorResponse = createErrorResponse(
        e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), extractCauses(e), request.getRequestURI());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
  }

  @ExceptionHandler(DaoLayerException.class)
  public ResponseEntity<ErrorResponseDto> handleDaoLayerException(
      DaoLayerException e, HttpServletRequest request) {
    log.error("Data access error: {}", e.getMessage(), e);
    ErrorResponseDto errorResponse = createErrorResponse(
        "Data access error occurred", HttpStatus.INTERNAL_SERVER_ERROR.value(), extractCauses(e), request.getRequestURI());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
  }

  @ExceptionHandler(OtpGenerationFailedException.class)
  public ResponseEntity<ErrorResponseDto> handleOtpGenerationFailedException(
      OtpGenerationFailedException e, HttpServletRequest request) {
    log.error("OTP generation failed: {}", e.getMessage(), e);
    ErrorResponseDto errorResponse = createErrorResponse(
        "Failed to generate OTP. Please try again.", HttpStatus.INTERNAL_SERVER_ERROR.value(),
        extractCauses(e), request.getRequestURI());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
  }

  @ExceptionHandler(EmailServiceException.class)
  public ResponseEntity<ErrorResponseDto> handleEmailServiceException(
      EmailServiceException e, HttpServletRequest request) {
    log.error("Email service error: {}", e.getMessage(), e);
    ErrorResponseDto errorResponse = createErrorResponse(
        "Failed to send email. Please try again later.", HttpStatus.SERVICE_UNAVAILABLE.value(),
        extractCauses(e), request.getRequestURI());
    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorResponse);
  }

  @ExceptionHandler(JwtTokenGenerationFailedException.class)
  public ResponseEntity<ErrorResponseDto> handleJwtTokenGenerationFailedException(
      JwtTokenGenerationFailedException e, HttpServletRequest request) {
    log.error("JWT token generation failed: {}", e.getMessage(), e);
    ErrorResponseDto errorResponse = createErrorResponse(
        "Authentication token generation failed", HttpStatus.INTERNAL_SERVER_ERROR.value(),
        extractCauses(e), request.getRequestURI());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
  }

  @ExceptionHandler(JwtTokenParseException.class)
  public ResponseEntity<ErrorResponseDto> handleJwtTokenParseException(
      JwtTokenParseException e, HttpServletRequest request) {
    log.warn("JWT token parse error: {}", e.getMessage());
    ErrorResponseDto errorResponse = createErrorResponse(
        "Invalid authentication token", HttpStatus.UNAUTHORIZED.value(),
        extractCauses(e), request.getRequestURI());
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
  }

  // Spring Security Authentication Exceptions

  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<ErrorResponseDto> handleAuthenticationException(
      AuthenticationException e, HttpServletRequest request) {
    log.warn("Authentication failed: {}", e.getMessage());
    ErrorResponseDto errorResponse = createErrorResponse(
        "Authentication failed: " + e.getMessage(), HttpStatus.UNAUTHORIZED.value(),
        extractCauses(e), request.getRequestURI());
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
  }

  @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
  public ResponseEntity<ErrorResponseDto> handleAccessDeniedException(
      org.springframework.security.access.AccessDeniedException e, HttpServletRequest request) {
    log.warn("Access denied: {}", e.getMessage());
    String userMessage = getCleanExceptionMessage(e);
    if (userMessage.isEmpty() || userMessage.toLowerCase().contains("access denied")) {
      userMessage = "You don't have permission to access this resource";
    }
    ErrorResponseDto errorResponse = createErrorResponse(
        userMessage, HttpStatus.FORBIDDEN.value(), extractCauses(e), request.getRequestURI());
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
  }

  @ExceptionHandler(org.springframework.security.authorization.AuthorizationDeniedException.class)
  public ResponseEntity<ErrorResponseDto> handleAuthorizationDeniedException(
      org.springframework.security.authorization.AuthorizationDeniedException e, HttpServletRequest request) {
    log.warn("Authorization denied: {}", e.getMessage());

    String userMessage = "You are not authorized to perform this action";

    // Try to extract more specific information from the exception
    if (e.getMessage() != null && !e.getMessage().trim().isEmpty()) {
      String cleanMessage = getCleanExceptionMessage(e);
      if (!cleanMessage.isEmpty() && !cleanMessage.toLowerCase().contains("authorization denied")) {
        userMessage = "Authorization denied: " + cleanMessage;
      }
    }

    ErrorResponseDto errorResponse = createErrorResponse(
        userMessage, HttpStatus.FORBIDDEN.value(), extractCauses(e), request.getRequestURI());
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
  }

  // Validation Exceptions

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException e, HttpServletRequest request) {
    log.warn("Validation failed: {}", e.getMessage());

    List<String> validationErrors = e.getBindingResult().getFieldErrors()
        .stream()
        .map(error -> error.getField() + ": " + error.getDefaultMessage())
        .collect(Collectors.toList());

    ErrorResponseDto errorResponse = createErrorResponse(
        "Validation failed", HttpStatus.BAD_REQUEST.value(),
        validationErrors, request.getRequestURI());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  @ExceptionHandler(BindException.class)
  public ResponseEntity<ErrorResponseDto> handleBindException(
      BindException e, HttpServletRequest request) {
    log.warn("Binding validation failed: {}", e.getMessage());

    List<String> bindingErrors = e.getBindingResult().getFieldErrors()
        .stream()
        .map(error -> error.getField() + ": " + error.getDefaultMessage())
        .collect(Collectors.toList());

    ErrorResponseDto errorResponse = createErrorResponse(
        "Request binding failed", HttpStatus.BAD_REQUEST.value(),
        bindingErrors, request.getRequestURI());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  // HTTP Method and Request Handling Exceptions

  @ExceptionHandler(NoHandlerFoundException.class)
  public ResponseEntity<ErrorResponseDto> handleNoHandlerFoundException(
      NoHandlerFoundException e, HttpServletRequest request) {
    log.warn("No handler found for {} {}", e.getHttpMethod(), e.getRequestURL());
    String message = String.format("Endpoint not found: %s %s", e.getHttpMethod(), e.getRequestURL());
    ErrorResponseDto errorResponse = createErrorResponse(
        message, HttpStatus.NOT_FOUND.value(), extractCauses(e), request.getRequestURI());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<ErrorResponseDto> handleHttpRequestMethodNotSupportedException(
      HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
    log.warn("Method not supported: {} for {}", e.getMethod(), request.getRequestURI());

    String supportedMethods = e.getSupportedHttpMethods() != null ?
        e.getSupportedHttpMethods().toString() : "GET, POST, PUT, DELETE";

    String message = String.format("HTTP method '%s' is not supported for this endpoint. Supported methods: %s",
        e.getMethod(), supportedMethods);

    ErrorResponseDto errorResponse = createErrorResponse(
        message, HttpStatus.METHOD_NOT_ALLOWED.value(), extractCauses(e), request.getRequestURI());
    return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorResponse);
  }

  @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
  public ResponseEntity<ErrorResponseDto> handleHttpMediaTypeNotSupportedException(
      HttpMediaTypeNotSupportedException e, HttpServletRequest request) {
    log.warn("Media type not supported: {}", e.getContentType());

    String supportedTypes = e.getSupportedMediaTypes().isEmpty() ?
        "application/json" : e.getSupportedMediaTypes().toString();

    String message = String.format("Media type '%s' is not supported. Supported types: %s",
        e.getContentType(), supportedTypes);

    ErrorResponseDto errorResponse = createErrorResponse(
        message, HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), extractCauses(e), request.getRequestURI());
    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(errorResponse);
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<ErrorResponseDto> handleMissingServletRequestParameterException(
      MissingServletRequestParameterException e, HttpServletRequest request) {
    log.warn("Missing required parameter: {} of type {}", e.getParameterName(), e.getParameterType());

    String message = String.format("Required parameter '%s' of type '%s' is missing",
        e.getParameterName(), e.getParameterType());

    ErrorResponseDto errorResponse = createErrorResponse(
        message, HttpStatus.BAD_REQUEST.value(), extractCauses(e), request.getRequestURI());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ErrorResponseDto> handleMethodArgumentTypeMismatchException(
      MethodArgumentTypeMismatchException e, HttpServletRequest request) {
    log.warn("Method argument type mismatch: parameter '{}' with value '{}' could not be converted to type '{}'",
        e.getName(), e.getValue(), e.getRequiredType() != null ? e.getRequiredType().getSimpleName() : "unknown");

    String message = String.format("Invalid value '%s' for parameter '%s'. Expected type: %s",
        e.getValue(), e.getName(),
        e.getRequiredType() != null ? e.getRequiredType().getSimpleName() : "unknown");

    ErrorResponseDto errorResponse = createErrorResponse(
        message, HttpStatus.BAD_REQUEST.value(), extractCauses(e), request.getRequestURI());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  @ExceptionHandler(NoSuchMethodException.class)
  public ResponseEntity<ErrorResponseDto> handleNoSuchMethodException(
      NoSuchMethodException e, HttpServletRequest request) {
    log.error("Method not found: {}", e.getMessage(), e);

    String cleanMessage = getCleanExceptionMessage(e);
    String message = cleanMessage.isEmpty() ?
        "Requested method is not available" :
        "Method not found: " + cleanMessage;

    ErrorResponseDto errorResponse = createErrorResponse(
        message, HttpStatus.NOT_FOUND.value(), extractCauses(e), request.getRequestURI());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
  }

  // Generic Exception Handler (catch-all)

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponseDto> handleGeneralException(
      Exception e, HttpServletRequest request) {
    log.error("Unexpected error occurred: {}", e.getMessage(), e);
    ErrorResponseDto errorResponse = createErrorResponse(
        "An unexpected error occurred. Please contact support if the problem persists.",
        HttpStatus.INTERNAL_SERVER_ERROR.value(), extractCauses(e), request.getRequestURI());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
  }

  // Helper Methods

  /**
   * Creates a standardized error response with the given parameters.
   */
  private ErrorResponseDto createErrorResponse(String message, int code, List<String> causes, String path) {
    ErrorResponseDto errorResponse = new ErrorResponseDto(message, code, causes);
    errorResponse.setPath(path);
    errorResponse.setTimestamp(Instant.now());
    return errorResponse;
  }

  /**
   * Extracts meaningful messages from nested exceptions, avoiding internal implementation details.
   */
  private List<String> extractCauses(Throwable throwable) {
    List<String> causes = new ArrayList<>();
    Throwable[] suppressed = throwable.getSuppressed();

    for (Throwable exception: suppressed){
        String message = getCleanExceptionMessage(exception);
        if (message != null && !message.trim().isEmpty() && !causes.contains(message)) {
            causes.add(message);
        }
    }
    return causes;
  }

  /**
   * Extracts a clean, user-friendly message from an exception.
   * Returns the exception message if available, otherwise a default message based on exception type.
   */
  private String getCleanExceptionMessage(Throwable throwable) {
    String message = throwable.getMessage();

    // If we have a meaningful message, clean it up
    if (message != null && !message.trim().isEmpty()) {
      // Remove common internal details that shouldn't be exposed to users
      message = cleanupInternalDetails(message);
      return message;
    }

    // Provide user-friendly default messages for common exception types
    return getDefaultMessageForExceptionType(throwable);
  }

  /**
   * Removes internal implementation details from exception messages.
   */
  private String cleanupInternalDetails(String message) {
    // Remove SQL error codes and technical details
    message = message.replaceAll("\\b(SQL|sql)\\s*[Ee]rror\\s*\\d+", "Database error");
    message = message.replaceAll("\\b[A-Za-z\\.]+Exception:\\s*", "");
    message = message.replaceAll("\\bat\\s+[a-zA-Z0-9\\.\\$]+\\([^)]*\\)", "");
    message = message.replaceAll("\\bcaused by:\\s*", "");
    message = message.replaceAll("\\bcom\\.spring\\.security\\.[\\w\\.]+", "");
    message = message.replaceAll("\\bjava\\.[\\w\\.]+", "");
    message = message.replaceAll("\\borg\\.[\\w\\.]+", "");

    // Remove stack trace references
    message = message.replaceAll("\\s+at\\s+.*", "");

    // Clean up multiple spaces
    message = message.replaceAll("\\s+", " ").trim();

    return message;
  }

  /**
   * Provides user-friendly default messages for specific exception types.
   */
  private String getDefaultMessageForExceptionType(Throwable throwable) {
    String className = throwable.getClass().getSimpleName();

    return switch (className) {
      case "SQLException", "DataAccessException" -> "Database operation failed";
      case "ConnectException", "SocketTimeoutException" -> "Connection timeout";
      case "AccessDeniedException" -> "Access denied";
      case "ValidationException" -> "Invalid input provided";
      case "IllegalArgumentException" -> "Invalid argument";
      case "IllegalStateException" -> "Invalid operation state";
      case "NullPointerException" -> "Required value is missing";
      case "NumberFormatException" -> "Invalid number format";
      case "ParseException" -> "Unable to parse input";
      case "FileNotFoundException" -> "Resource not found";
      case "IOException" -> "Input/output operation failed";
      case "TimeoutException" -> "Operation timed out";
      case "NoSuchMethodException" -> "Requested method is not available";
      case "NoHandlerFoundException" -> "Endpoint not found";
      case "HttpRequestMethodNotSupportedException" -> "HTTP method not supported";
      case "HttpMediaTypeNotSupportedException" -> "Media type not supported";
      case "MissingServletRequestParameterException" -> "Required parameter is missing";
      case "MethodArgumentTypeMismatchException" -> "Invalid parameter type";
      default -> "An error occurred during processing";
    };
  }

  /**
   * Gets the best available message from an exception, with fallback to default.
   */
  private String getExceptionMessage(Throwable throwable, String defaultMessage) {
    String message = throwable.getMessage();

    if (message != null && !message.trim().isEmpty()) {
      return cleanupInternalDetails(message);
    }

    return defaultMessage;
  }
}
