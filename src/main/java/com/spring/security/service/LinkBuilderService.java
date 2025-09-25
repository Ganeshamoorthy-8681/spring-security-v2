package com.spring.security.service;

/**
 * Service interface for building verification and other application links. Follows Single
 * Responsibility Principle by handling only URL construction.
 */
public interface LinkBuilderService {

  /**
   * Builds a user verification link with OTP and account ID as query parameters.
   *
   * @param otp the OTP code
   * @param accountId the account ID
   * @param email the user's email address
   * @return the complete verification URL
   */
  String buildUserVerificationLink(String otp, Long accountId, String email);

  /**
   * Builds a custom link with the specified path and parameters.
   *
   * @param path the path to append to base URL
   * @param otp the OTP code
   * @param accountId the account ID
   * @param email the user's email address
   * @return the complete URL
   */
  String buildCustomLink(String path, String otp, Long accountId, String email);
}
