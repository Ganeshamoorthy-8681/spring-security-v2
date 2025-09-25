package com.spring.security.service;

import com.spring.security.exceptions.EmailServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

/**
 * EmailServiceImpl is a service class that implements the EmailService interface. It provides
 * functionality to send emails using JavaMailSender.
 */
@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

  private final JavaMailSender javaMailSender;

  /**
   * Constructor for EmailServiceImpl.
   *
   * @param javaMailSender the JavaMailSender instance used to send emails
   */
  public EmailServiceImpl(JavaMailSender javaMailSender) {
    this.javaMailSender = javaMailSender;
  }

  /**
   * Sends an email with the specified subject and content to the given recipient.
   *
   * @param to the recipient's email address
   * @param subject the subject of the email
   * @param content the content of the email
   */
  @Override
  @Async("mailTaskExecutor")
  public void sendEmail(String to, String subject, String content) throws EmailServiceException {
    try {
      log.info("Sending email to: {}", to);
      SimpleMailMessage message = createEmail(to, subject, content);
      javaMailSender.send(message);
    } catch (Exception e) {
      // Log the exception (logging not shown here for brevity)
      log.error("Failed to send email to {}: {}", to, e.getMessage());
      throw new EmailServiceException("Failed to send email", e);
    }
  }

  /**
   * Sends an HTML email with the specified subject and HTML content to the given recipient.
   *
   * @param to the recipient's email address
   * @param subject the subject of the email
   * @param htmlContent the HTML content of the email
   */
  @Override
  @Async("mailTaskExecutor")
  public void sendHtmlEmail(String to, String subject, String htmlContent) throws EmailServiceException {
    try {
      log.info("Sending HTML email to: {}", to);
      MimeMessage message = createHtmlEmail(to, subject, htmlContent);
      javaMailSender.send(message);
    } catch (Exception e) {
      log.error("Failed to send HTML email to {}: {}", to, e.getMessage());
      throw new EmailServiceException("Failed to send HTML email", e);
    }
  }

  /**
   * Creates a SimpleMailMessage with the specified recipient, subject, and content.
   *
   * @param to the recipient's email address
   * @param subject the subject of the email
   * @param content the content of the email
   * @return a SimpleMailMessage object ready to be sent
   */
  private SimpleMailMessage createEmail(String to, String subject, String content) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(to);
    message.setSubject(subject);
    message.setText(content);
    return message;
  }

  /**
   * Creates a MimeMessage with HTML content for the specified recipient, subject, and content.
   *
   * @param to the recipient's email address
   * @param subject the subject of the email
   * @param htmlContent the HTML content of the email
   * @return a MimeMessage object ready to be sent
   */
  private MimeMessage createHtmlEmail(String to, String subject, String htmlContent) throws MessagingException {
    MimeMessage message = javaMailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

    helper.setTo(to);
    helper.setSubject(subject);
    helper.setText(htmlContent, true); // true indicates HTML content

    return message;
  }
}
