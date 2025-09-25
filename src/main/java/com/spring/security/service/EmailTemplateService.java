package com.spring.security.service;

/**
 * Service interface for generating email templates.
 * Follows Single Responsibility Principle by handling only email template generation.
 */
public interface EmailTemplateService {

  /**
   * Generates a user creation email template with OTP verification link.
   *
   * @param userName the name of the user
   * @param email the user's email address
   * @param verificationLink the complete verification link with OTP and account ID
   * @param isRootUser whether this is for a root user creation
   * @return the HTML email content
   */
  String generateUserCreationTemplate(String userName, String email, String verificationLink, boolean isRootUser);

  /**
   * Generates a user creation email template with OTP displayed directly (for root users).
   *
   * @param userName the name of the user
   * @param email the user's email address
   * @param otp the OTP code to display directly in email
   * @param isRootUser whether this is for a root user creation
   * @return the HTML email content
   */
  String generateUserCreationTemplateWithOtp(String userName, String email, String otp, boolean isRootUser);

  /**
   * Generates a simple OTP email template.
   *
   * @param otp the OTP code
   * @return the HTML email content
   */
  String generateOtpTemplate(String otp);

  /**
   * Generates a resend OTP email template with verification link.
   *
   * @param userName the name of the user
   * @param email the user's email address
   * @param verificationLink the complete verification link with OTP and account ID
   * @param otp the OTP code
   * @param isRootUser whether this is for a root user
   * @return the HTML email content
   */
  String generateResendOtpTemplate(String userName, String email, String verificationLink, String otp, boolean isRootUser);

  /**
   * Generates a resend OTP email template with OTP displayed directly.
   *
   * @param userName the name of the user
   * @param email the user's email address
   * @param otp the OTP code to display directly in email
   * @param isRootUser whether this is for a root user
   * @return the HTML email content
   */
  String generateResendOtpTemplateWithOtp(String userName, String email, String otp, boolean isRootUser);
}
