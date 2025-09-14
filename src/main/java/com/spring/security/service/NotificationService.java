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
}
