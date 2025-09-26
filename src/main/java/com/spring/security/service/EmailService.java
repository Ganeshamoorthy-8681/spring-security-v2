package com.spring.security.service;

import com.spring.security.exceptions.EmailServiceException;
import com.spring.security.exceptions.ServiceLayerException;
import jakarta.mail.MessagingException;

/**
 * Service interface for sending emails.
 *
 * <p>This interface defines the contract for email-related operations, such as sending emails to
 * users.
 */
public interface EmailService {

  /**
   * Sends an HTML email with the specified subject and HTML content to the given recipient.
   *
   * @param to the recipient's email address
   * @param subject the subject of the email
   * @param htmlContent the HTML content of the email
   */
  void sendHtmlEmail(String to, String subject, String htmlContent) throws EmailServiceException;
}
