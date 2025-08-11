package com.spring.security.service;

import com.spring.security.controller.dto.request.LoginRequestDto;
import com.spring.security.controller.dto.request.RootLoginRequestDto;
import com.spring.security.exceptions.AuthenticationException;
import com.spring.security.exceptions.JwtTokenGenerationFailedException;

/**
 * Interface for the login service that handles user authentication. It provides methods to
 * authenticate users based on their credentials.
 */
public interface LoginService {

  /**
   * Method to authenticate a user based on email and password.
   *
   * @param rootLoginRequestDto the login request containing email and password
   * @return a string representing the authentication result, typically a JWT token
   */
  String authenticate(RootLoginRequestDto rootLoginRequestDto) throws AuthenticationException;

  /**
   * Method to authenticate a user based on email, password, and account ID.
   *
   * @param loginRequestDto the login request containing email, password, and account ID
   * @return a string representing the authentication result, typically a JWT token
   */
  String authenticate(LoginRequestDto loginRequestDto) throws AuthenticationException;
}
