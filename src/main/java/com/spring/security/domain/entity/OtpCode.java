package com.spring.security.domain.entity;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OtpCode {

  private String otp;

  private String email;

  private LocalDateTime createdAt;

  private LocalDateTime expiresAt;
}
