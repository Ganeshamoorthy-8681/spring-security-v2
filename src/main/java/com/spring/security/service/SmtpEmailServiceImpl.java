package com.spring.security.service;

import com.spring.security.exceptions.EmailServiceException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/** SMTP email service implementation that is conditionally activated in development profile. */
@Service
@Profile("dev")
@Slf4j
public class SmtpEmailServiceImpl implements EmailService {

  private final JavaMailSender javaMailSender;

  public SmtpEmailServiceImpl(JavaMailSender javaMailSender) {
    this.javaMailSender = javaMailSender;
  }

  @Override
  @Async("mailTaskExecutor")
  public void sendHtmlEmail(String to, String subject, String htmlContent)
      throws EmailServiceException {
    try {
      log.info("Sending HTML email to: {} using SMTP service", to);
      MimeMessage message = createHtmlEmail(to, subject, htmlContent);
      javaMailSender.send(message);
    } catch (Exception e) {
      log.error("Failed to send HTML SMTP email to {}: {}", to, e.getMessage());
      throw new EmailServiceException("Failed to send Email", e);
    }
  }

  private MimeMessage createHtmlEmail(String to, String subject, String htmlContent)
      throws MessagingException {
    MimeMessage message = javaMailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

    helper.setTo(to);
    helper.setSubject(subject);
    helper.setText(htmlContent, true);

    return message;
  }
}
