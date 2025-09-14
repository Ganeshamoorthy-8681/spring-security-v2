package com.spring.security.domain.entity;

import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OtpCode {

  private Long id;

  private String otp;

  private Boolean used;

  private String email;

  private Instant createdAt;

  private Instant expiresAt;
}
