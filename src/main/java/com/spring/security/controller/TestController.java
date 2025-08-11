package com.spring.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TestController is a simple REST controller for testing purposes.
 * It provides an endpoint to verify that authentication.
 */
@RestController
public class TestController {

  @GetMapping("/api/v1/test")
  public String test() {
    return "Test endpoint is working!";
  }
}
