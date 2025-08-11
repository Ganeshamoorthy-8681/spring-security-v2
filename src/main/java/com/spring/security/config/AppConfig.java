package com.spring.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/** AppConfig is a configuration class that defines beans for the application. */
@Configuration
public class AppConfig {

  /**
   * passwordEncoder bean is used to encode passwords using BCrypt hashing algorithm.
   *
   * @return a BCryptPasswordEncoder instance with a strength of 12
   */
  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(12);
  }
}
