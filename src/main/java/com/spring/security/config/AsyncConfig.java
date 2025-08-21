package com.spring.security.config;

import java.util.concurrent.Executor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {

  @Bean(name = "mailTaskExecutor")
  public Executor mailTaskExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(5); // Minimum number of threads
    executor.setMaxPoolSize(10); // Max threads in pool
    executor.setQueueCapacity(100); // Max queued tasks before rejecting
    executor.setThreadNamePrefix("MailSender-");
    executor.initialize();
    return executor;
  }
}
