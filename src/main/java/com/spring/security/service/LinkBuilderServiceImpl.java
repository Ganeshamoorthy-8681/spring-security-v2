package com.spring.security.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Implementation of LinkBuilderService that constructs application URLs. Follows Single
 * Responsibility Principle by focusing only on URL building.
 */
@Service
public class LinkBuilderServiceImpl implements LinkBuilderService {

  private final String uiBaseUrl;

  /**
   * Constructor that injects the UI base URL from application properties.
   *
   * @param uiBaseUrl the base URL from application.properties
   */
  public LinkBuilderServiceImpl(@Value("${UI.BASE.URL}") String uiBaseUrl) {
    this.uiBaseUrl = uiBaseUrl;
  }

  @Override
  public String buildUserVerificationLink(String otp, Long accountId, String email) {
    return buildCustomLink("/verify", otp, accountId, email);
  }

  @Override
  public String buildCustomLink(String path, String otp, Long accountId, String email) {
    return UriComponentsBuilder.fromUriString(uiBaseUrl)
        .path(path)
        .queryParam("otp", otp)
        .queryParam("accountId", accountId)
        .queryParam("email", email)
        .build()
        .toUriString();
  }
}
