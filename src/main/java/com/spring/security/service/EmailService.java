package com.spring.security.service;

import com.spring.security.exceptions.EmailServiceException;

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
