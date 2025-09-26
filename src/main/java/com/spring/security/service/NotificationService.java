package com.spring.security.service;

import com.spring.security.exceptions.ServiceLayerException;

/**
 * Service interface for managing email notifications within the application. Provides simple,
 * straightforward methods for common notification scenarios.
 */
public interface NotificationService {

  /**
   * Sends account creation successful notification with OTP (only for root users). For regular
   * users, this sends a success notification without OTP.
   *
   * @param userName the name of the user
   * @param email the user's email address
   * @param otp the OTP code for verification (only used for root users)
   * @throws ServiceLayerException if there is an error during the process
   */
  void sendAccountCreationSuccessfulWithOtp(String userName, String email, String otp)
      throws ServiceLayerException;

  /**
   * Sends OTP for IAM user operations (generic OTP sending). Used for user verification, password
   * reset, etc.
   *
   * @param userName the name of the user
   * @param email the user's email address
   * @param verificationLink the verification link containing the OTP
   * @throws ServiceLayerException if there is an error during the process
   */
  void sendUserCreationWithLink(String userName, String email, String verificationLink)
      throws ServiceLayerException;

  /**
   * Resends OTP when user requests a new one. Generic method for all OTP resend scenarios.
   *
   * @param userName the name of the user
   * @param email the user's email address
   * @param otp the new OTP code
   * @throws ServiceLayerException if there is an error during the process
   */
  void resendOtpForAccountCreation(String userName, String email, String otp)
      throws ServiceLayerException;

  /**
   * Resends OTP for user creation when user requests a new one. Generic method for all OTP resend
   * scenarios.
   *
   * @param userName the name of the user
   * @param email the user's email address
   * @param verificationLink the verification link containing the OTP
   * @throws ServiceLayerException if there is an error during the process
   */
  void resendOtpForUserCreation(String userName, String email, String verificationLink)
      throws ServiceLayerException;
}
