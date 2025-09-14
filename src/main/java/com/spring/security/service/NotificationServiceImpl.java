package com.spring.security.service;

import com.spring.security.exceptions.ServiceLayerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Service implementation for handling notification operations, specifically sending OTPs via email.
 */
@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {

  private final EmailService emailService;

  /**
   * Constructs a NotificationServiceImpl with the necessary dependencies.
   *
   * @param emailService the email service to send notifications
   */
  public NotificationServiceImpl(EmailService emailService) {
    this.emailService = emailService;
  }

  /**
   * Verifies the email address of an account.
   *
   * @param email the email address to verify
   */
  public void sendOtp(String otp, String email) throws ServiceLayerException {
    try {
      emailService.sendEmail(email, "Your OTP Code", "Your OTP code is: " + otp);
      log.debug("OTP sent to email: {}", email);
    } catch (Exception e) {
      log.error("Failed to send OTP to email {}: {}", email, e.getMessage());
      throw new ServiceLayerException("Failed to send OTP to email", e);
    }
  }
}
