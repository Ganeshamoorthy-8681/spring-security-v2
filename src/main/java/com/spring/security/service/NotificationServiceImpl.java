package com.spring.security.service;

import com.spring.security.exceptions.ServiceLayerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Service implementation for handling notification operations with simplified, straightforward methods.
 */
@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {

  private final EmailService emailService;

  private final EmailTemplateService emailTemplateService;

  /**
   * Constructs a NotificationServiceImpl with the necessary dependencies.
   *
   * @param emailService the email service to send notifications
   * @param emailTemplateService the service for generating email templates
   */
  public NotificationServiceImpl(
      EmailService emailService,
      EmailTemplateService emailTemplateService) {
    this.emailService = emailService;
    this.emailTemplateService = emailTemplateService;
  }

  /**
   * Sends account creation successful notification with OTP.
   * Uses account-created-success template.
   */
  @Override
  public void sendAccountCreationSuccessfulWithOtp(String userName, String email, String otp)
      throws ServiceLayerException {
    try {
      String htmlContent = emailTemplateService.generateAccountCreationTemplate(userName, email, otp);
      String subject = "Account Created Successfully - Set Up Your Password";
      log.info("Sending account creation email with OTP to: {}", email);

      emailService.sendHtmlEmail(email, subject, htmlContent);
      log.info("Account creation email successfully sent to: {}", email);

    } catch (Exception e) {
      log.error("Failed to send account creation email to {}: {}", email, e.getMessage());
      throw new ServiceLayerException("Failed to send account creation notification", e);
    }
  }

  /**
   * Sends user creation notification with verification link.
   * Uses account-setup-otp template.
   */
  @Override
  public void sendUserCreationWithLink(String userName, String email, String verificationLink)
      throws ServiceLayerException {
    try {
      log.info("Sending user creation email with verification link to: {}", email);
      String htmlContent = emailTemplateService.generateUserCreationTemplate(userName, email, verificationLink);
      String subject = "User Created - Set Up Your Password";

      emailService.sendHtmlEmail(email, subject, htmlContent);
      log.info("User creation email successfully sent to: {}", email);
    } catch (Exception e) {
      log.error("Failed to send user creation email to {}: {}", email, e.getMessage());
      throw new ServiceLayerException("Failed to send user creation notification", e);
    }
  }

  /**
   * Resends OTP for account creation when user requests a new one.
   * Uses resend-otp template.
   */
  @Override
  public void resendOtpForAccountCreation(String userName, String email, String otp)
      throws ServiceLayerException {
    try {
      log.info("Resending OTP for account creation to user: {}", email);
      String htmlContent = emailTemplateService.generateAccountCreationResendOtp(userName, email, otp);
      String subject = "Your New Verification Code";

      emailService.sendHtmlEmail(email, subject, htmlContent);
      log.info("OTP successfully resent for account creation to user: {}", email);
    } catch (Exception e) {
      log.error("Failed to resend OTP for account creation to {}: {}", email, e.getMessage());
      throw new ServiceLayerException("Failed to resend OTP for account creation", e);
    }
  }

  /**
   * Resends OTP for user creation with verification link when user requests a new one.
   * Uses resend-otp-link template.
   */
  @Override
  public void resendOtpForUserCreation(String userName, String email, String verificationLink)
      throws ServiceLayerException {
    try {
      log.info("Resending verification link for user creation to: {}", email);
      String htmlContent = emailTemplateService.generateUserCreationResendOtp(userName, email, verificationLink);
      String subject = "Your New Verification Link";

      emailService.sendHtmlEmail(email, subject, htmlContent);
      log.info("Verification link successfully resent for user creation to: {}", email);
    } catch (Exception e) {
      log.error("Failed to resend verification link for user creation to {}: {}", email, e.getMessage());
      throw new ServiceLayerException("Failed to resend verification link for user creation", e);
    }
  }
}
