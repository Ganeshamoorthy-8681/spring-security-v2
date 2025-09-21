package com.spring.security.controller;

import com.spring.security.controller.dto.request.LoginRequestDto;
import com.spring.security.controller.dto.request.RootLoginRequestDto;
import com.spring.security.controller.dto.response.UserResponseDto;
import com.spring.security.domain.entity.User;
import com.spring.security.domain.mapper.UserMapper;
import com.spring.security.exceptions.AuthenticationException;
import com.spring.security.exceptions.JwtTokenParseException;
import com.spring.security.exceptions.PreconditionViolationException;
import com.spring.security.exceptions.ServiceLayerException;
import com.spring.security.service.LoginService;
import com.spring.security.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling authentication-related operations. Provides endpoints for user login and
 * root user login.
 */
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

  private final LoginService loginService;

  private final UserService userService;

  /**
   * Constructor for AuthController.
   *
   * @param loginService the AuthenticationManager to handle authentication logic
   */
  public AuthController(LoginService loginService, UserService userService) {
    this.loginService = loginService;
    this.userService = userService;
  }

  /**
   * Endpoint for user login.
   *
   * @param requestDto the login request containing account ID, email, and password
   * @return ResponseEntity with HTTP status CREATED (201) if login is successful, or UNAUTHORIZED
   *     (401) if login fails
   */
  @PostMapping("/login")
  public ResponseEntity<Void> login(
      @RequestBody LoginRequestDto requestDto, HttpServletResponse response)
      throws AuthenticationException {

    String token = loginService.authenticate(requestDto);
    response.setHeader("Authorization", "Bearer " + token);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  /**
   * Endpoint for root user login.
   *
   * @param requestDto the root login request containing email and password
   * @return ResponseEntity with HTTP status CREATED (201) if login is successful, or UNAUTHORIZED
   *     (401) if login fails
   */
  @PostMapping("/root-login")
  public ResponseEntity<Void> rootLogin(
      @RequestBody RootLoginRequestDto requestDto, HttpServletResponse response)
      throws AuthenticationException {

    String token = loginService.authenticate(requestDto);
    if (token == null) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    response.setHeader("Authorization", "Bearer " + token);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @GetMapping("/whoami")
  public ResponseEntity<UserResponseDto> whoAmI(@RequestHeader("Authorization") String jwtToken)
      throws JwtTokenParseException, ServiceLayerException, PreconditionViolationException {
    User userInfo = userService.whoami(jwtToken);
    return new ResponseEntity<>(UserMapper.USER_MAPPER.convertUserToUserResponseDto(userInfo), HttpStatus.OK);
  }
}
