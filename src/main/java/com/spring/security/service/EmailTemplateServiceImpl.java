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
  public String generateUserCreationTemplate(
      String userName, String email, String verificationLink, boolean isRootUser) {
    String userType = isRootUser ? "Root User" : "User";
    String welcomeMessage =
        isRootUser
            ? "You have been granted root user access to our system."
            : "Welcome to our platform! Your account has been created.";

    Context context = new Context();
    context.setVariable("userName", userName);
    context.setVariable("email", email);
    context.setVariable("verificationLink", verificationLink);
    context.setVariable("userType", userType);
    context.setVariable("welcomeMessage", welcomeMessage);

    return templateEngine.process("emails/user-creation", context);
  }

  @Override
  public String generateUserCreationTemplateWithOtp(
      String userName, String email, String otp, boolean isRootUser) {
    String userType = isRootUser ? "Root User" : "User";
    String welcomeMessage =
        isRootUser
            ? "You have been granted root user access to our system."
            : "Welcome to our platform! Your account has been created.";

    Context context = new Context();
    context.setVariable("userName", userName);
    context.setVariable("email", email);
    context.setVariable("otp", otp);
    context.setVariable("userType", userType);
    context.setVariable("welcomeMessage", welcomeMessage);

    return templateEngine.process("emails/user-creation", context);
  }

  @Override
  public String generateOtpTemplate(String otp) {
    Context context = new Context();
    context.setVariable("otp", otp);

    return templateEngine.process("emails/simple-otp", context);
  }

  @Override
  public String generateResendOtpTemplate(
      String userName, String email, String verificationLink, String otp, boolean isRootUser) {
    String userType = isRootUser ? "Root User" : "User";

    Context context = new Context();
    context.setVariable("userName", userName);
    context.setVariable("email", email);
    context.setVariable("verificationLink", verificationLink);
    context.setVariable("otp", otp);
    context.setVariable("userType", userType);

    return templateEngine.process("emails/resend-otp", context);
  }

  @Override
  public String generateResendOtpTemplateWithOtp(
      String userName, String email, String otp, boolean isRootUser) {
    String userType = isRootUser ? "Root User" : "User";

    Context context = new Context();
    context.setVariable("userName", userName);
    context.setVariable("email", email);
    context.setVariable("otp", otp);
    context.setVariable("userType", userType);

    return templateEngine.process("emails/resend-otp", context);
  }
}
