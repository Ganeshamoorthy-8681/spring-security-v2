package com.spring.security.service;

import com.spring.security.exceptions.EmailServiceException;

/**
 * Service interface for sending emails.
 *
 * <p>This interface defines the contract for email-related operations, such as sending emails to
 * users.
 */
public interface EmailService {

  /**
   * Sends an email with the specified subject and content to the given recipient.
   *
   * @param to the recipient's email address
   * @param subject the subject of the email
   * @param content the content of the email
   */
  void sendEmail(String to, String subject, String content) throws EmailServiceException;
}
