package com.spring.security.service;

import com.spring.security.exceptions.ServiceLayerException;

/**
 * Service interface for managing notifications within the application.
 *
 * <p>This interface defines the contract for notification-related operations, such as sending
 * notifications to users via various channels (e.g., email, SMS, push notifications).
 */
public interface NotificationService {

  /**
   * Sends an OTP to the specified email address.
   *
   * @param otp the One-Time Password to be sent
   * @param email the email address to which the OTP should be sent
   * @throws ServiceLayerException if there is an error during the process
   */
  void sendOtp(String otp, String email) throws ServiceLayerException;

  /**
   * Sends a user creation email with verification link containing OTP and account ID.
   *
   * @param userName the name of the user
   * @param email the user's email address
   * @param otp the OTP code for verification
   * @param accountId the account ID
   * @param isRootUser whether this is for a root user creation
   * @throws ServiceLayerException if there is an error during the process
   */
  void sendUserCreationEmail(String userName, String email, String otp, Long accountId, boolean isRootUser) throws ServiceLayerException;

  /**
   * Sends a user creation email with OTP displayed directly (for root users).
   *
   * @param userName the name of the user
   * @param email the user's email address
   * @param otp the OTP code to display directly in email
   * @param isRootUser whether this is for a root user creation
   * @throws ServiceLayerException if there is an error during the process
   */
  void sendUserCreationEmailWithOtp(String userName, String email, String otp, boolean isRootUser) throws ServiceLayerException;

  /**
   * Sends a resend OTP email with professional template and verification link.
   *
   * @param userName the name of the user
   * @param email the user's email address
   * @param otp the OTP code for verification
   * @param accountId the account ID
   * @param isRootUser whether this is for a root user
   * @throws ServiceLayerException if there is an error during the process
   */
  void sendResendOtpEmail(String userName, String email, String otp, Long accountId, boolean isRootUser) throws ServiceLayerException;

  /**
   * Sends a resend OTP email with OTP displayed directly.
   *
   * @param userName the name of the user
   * @param email the user's email address
   * @param otp the OTP code to display directly in email
   * @param isRootUser whether this is for a root user
   * @throws ServiceLayerException if there is an error during the process
   */
  void sendResendOtpEmailWithOtp(String userName, String email, String otp, boolean isRootUser) throws ServiceLayerException;
}
