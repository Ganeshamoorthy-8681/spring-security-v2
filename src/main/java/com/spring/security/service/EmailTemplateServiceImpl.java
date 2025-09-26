package com.spring.security.service;

import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * Implementation of EmailTemplateService that generates HTML email templates using Thymeleaf.
 * Follows Single Responsibility Principle by focusing only on template generation.
 */
@Service
public class EmailTemplateServiceImpl implements EmailTemplateService {

  private final TemplateEngine templateEngine;

  /**
   * Constructor that injects Thymeleaf TemplateEngine.
   *
   * @param templateEngine the Thymeleaf template engine
   */
  public EmailTemplateServiceImpl(TemplateEngine templateEngine) {
    this.templateEngine = templateEngine;
  }

  @Override
  public String generateAccountCreationTemplate(String accountName, String email, String otp) {
    Context context = new Context();
    context.setVariable("userName", accountName);
    context.setVariable("email", email);
    context.setVariable("otp", otp);

    return templateEngine.process("emails/account-created-success", context);
  }

  @Override
  public String generateUserCreationTemplate(
      String userName, String email, String verificationLink) {
    Context context = new Context();
    context.setVariable("userName", userName);
    context.setVariable("email", email);
    context.setVariable("passwordSetupLink", verificationLink);

    return templateEngine.process("emails/account-setup-otp", context);
  }

  @Override
  public String generateAccountCreationResendOtp(String userName, String email, String otp) {
    Context context = new Context();
    context.setVariable("userName", userName);
    context.setVariable("email", email);
    context.setVariable("otp", otp);

    return templateEngine.process("emails/resend-otp", context);
  }

  @Override
  public String generateUserCreationResendOtp(
      String userName, String email, String verificationLink) {
    Context context = new Context();
    context.setVariable("userName", userName);
    context.setVariable("email", email);
    context.setVariable("verificationLink", verificationLink);

    return templateEngine.process("emails/resend-otp-link", context);
  }
}
