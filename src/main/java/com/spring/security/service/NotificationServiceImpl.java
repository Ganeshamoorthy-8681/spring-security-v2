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
  private final EmailTemplateService emailTemplateService;
  private final LinkBuilderService linkBuilderService;

  /**
   * Constructs a NotificationServiceImpl with the necessary dependencies.
   *
   * @param emailService the email service to send notifications
   * @param emailTemplateService the service for generating email templates
   * @param linkBuilderService the service for building verification links
   */
  public NotificationServiceImpl(EmailService emailService,
                                EmailTemplateService emailTemplateService,
                                LinkBuilderService linkBuilderService) {
    this.emailService = emailService;
    this.emailTemplateService = emailTemplateService;
    this.linkBuilderService = linkBuilderService;
  }

  /**
   * Verifies the email address of an account.
   *
   * @param email the email address to verify
   */
  public void sendOtp(String otp, String email) throws ServiceLayerException {
    try {
      String htmlContent = emailTemplateService.generateOtpTemplate(otp);
      emailService.sendHtmlEmail(email, "Your OTP Code", htmlContent);
      log.debug("OTP sent to email: {}", email);
    } catch (Exception e) {
      log.error("Failed to send OTP to email {}: {}", email, e.getMessage());
      throw new ServiceLayerException("Failed to send OTP to email", e);
    }
  }

  /**
   * Sends a user creation email with verification link containing OTP and account ID.
   *
   * @param userName the name of the user
   * @param email the user's email address
   * @param otp the OTP code for verification
   * @param accountId the account ID
   * @param isRootUser whether this is for a root user creation
   */
  @Override
  public void sendUserCreationEmail(String userName, String email, String otp, Long accountId, boolean isRootUser) throws ServiceLayerException {
    try {
      String verificationLink = linkBuilderService.buildUserVerificationLink(otp, accountId, email);
      String htmlContent = emailTemplateService.generateUserCreationTemplate(userName, email, verificationLink, isRootUser);

      String subject = isRootUser ? "Root User Account Created - Verification Required" : "Account Created - Verification Required";

      emailService.sendHtmlEmail(email, subject, htmlContent);
      log.debug("User creation email sent to: {} for account: {}", email, accountId);
    } catch (Exception e) {
      log.error("Failed to send user creation email to {}: {}", email, e.getMessage());
      throw new ServiceLayerException("Failed to send user creation email", e);
    }
  }

  /**
   * Sends a user creation email with OTP displayed directly in the email body.
   *
   * @param userName the name of the user
   * @param email the user's email address
   * @param otp the OTP code for verification
   * @param isRootUser whether this is for a root user creation
   */
  @Override
  public void sendUserCreationEmailWithOtp(String userName, String email, String otp, boolean isRootUser) throws ServiceLayerException {
    try {
      String htmlContent = emailTemplateService.generateUserCreationTemplateWithOtp(userName, email, otp, isRootUser);

      String subject = isRootUser ? "Root User Account Created - OTP Verification" : "Account Created - OTP Verification";

      emailService.sendHtmlEmail(email, subject, htmlContent);
      log.debug("User creation email with OTP sent to: {}", email);
    } catch (Exception e) {
      log.error("Failed to send user creation email with OTP to {}: {}", email, e.getMessage());
      throw new ServiceLayerException("Failed to send user creation email with OTP", e);
    }
  }

  /**
   * Sends a resend OTP email with professional template and verification link.
   *
   * @param userName the name of the user
   * @param email the user's email address
   * @param otp the OTP code for verification
   * @param accountId the account ID
   * @param isRootUser whether this is for a root user
   */
  @Override
  public void sendResendOtpEmail(String userName, String email, String otp, Long accountId, boolean isRootUser) throws ServiceLayerException {
    try {
      String verificationLink = linkBuilderService.buildUserVerificationLink(otp, accountId, email);
      String htmlContent = emailTemplateService.generateResendOtpTemplate(userName, email, verificationLink, otp, isRootUser);

      String subject = "Your New Verification Code";

      emailService.sendHtmlEmail(email, subject, htmlContent);
      log.debug("Resend OTP email sent to: {} for account: {}", email, accountId);
    } catch (Exception e) {
      log.error("Failed to send resend OTP email to {}: {}", email, e.getMessage());
      throw new ServiceLayerException("Failed to send resend OTP email", e);
    }
  }

  /**
   * Sends a resend OTP email with OTP displayed directly in the email body.
   *
   * @param userName the name of the user
   * @param email the user's email address
   * @param otp the OTP code for verification
   * @param isRootUser whether this is for a root user
   */
  @Override
  public void sendResendOtpEmailWithOtp(String userName, String email, String otp, boolean isRootUser) throws ServiceLayerException {
    try {
      String htmlContent = emailTemplateService.generateResendOtpTemplateWithOtp(userName, email, otp, isRootUser);

      String subject = "Your New Verification Code";

      emailService.sendHtmlEmail(email, subject, htmlContent);
      log.debug("Resend OTP email with OTP sent to: {}", email);
    } catch (Exception e) {
      log.error("Failed to send resend OTP email with OTP to {}: {}", email, e.getMessage());
      throw new ServiceLayerException("Failed to send resend OTP email with OTP", e);
    }
  }
}
