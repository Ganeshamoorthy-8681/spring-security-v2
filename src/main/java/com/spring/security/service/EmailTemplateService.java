package com.spring.security.service;

/**
 * Service interface for generating email templates. Follows Single Responsibility Principle by
 * handling only email template generation.
 */
public interface EmailTemplateService {

  /**
   * Generates an account creation email template with OTP.
   *
   * @param accountName account name
   * @param email email address
   * @param otp one-time password
   * @return the HTML email content
   */
  String generateAccountCreationTemplate(String accountName, String email, String otp);

  /**
   * Generates a user creation email template with OTP verification link.
   *
   * @param userName the name of the user
   * @param email the user's email address
   * @param verificationLink the complete verification link with OTP and account ID
   * @return the HTML email content
   */
  String generateUserCreationTemplate(String userName, String email, String verificationLink);

  /**
   * Generates a resend OTP email template with OTP code (for account creation).
   *
   * @param userName the name of the user
   * @param email the user's email address
   * @param otp the OTP code
   * @return the HTML email content
   */
  String generateAccountCreationResendOtp(String userName, String email, String otp);

  /**
   * Generates a resend OTP email template with verification link (for user creation).
   *
   * @param userName the name of the user
   * @param email the user's email address
   * @param verificationLink the complete verification link
   * @return the HTML email content
   */
  String generateUserCreationResendOtp(String userName, String email, String verificationLink);
}
