package com.spring.security.controller.dto.response;

public enum OtpValidationStatus {
    /** The OTP is valid. */
    VALID,

    /** The OTP is invalid. */
    INVALID,

    /** The OTP has expired. */
    EXPIRED,

    /** The OTP is not found. */
    NOT_FOUND
}
