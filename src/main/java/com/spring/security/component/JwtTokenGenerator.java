package com.spring.security.component;

import com.spring.security.exceptions.JwtTokenGenerationFailedException;
import com.spring.security.exceptions.JwtTokenParseException;
import com.spring.security.exceptions.PreconditionViolationException;
import io.jsonwebtoken.Claims;
import java.util.Map;

/**
 * Interface for generating JWT tokens. This interface defines a method to generate a JWT token
 * based on the user's email and roles.
 */
public interface JwtTokenGenerator {

  /**
   * Generates a JWT token for the given user.
   *
   * @param claims A map containing the claims to be included in the token.
   * @return A JWT token as a String.
   */
  String generate(Map<String, Object> claims) throws JwtTokenGenerationFailedException;

  /**
   * Parses the JWT token and retrieves the claims
   *
   * @param token The JWT token to be parsed.
   * @return Claims object containing the claims from the token.
   */
  Claims getClaims(String token) throws PreconditionViolationException, JwtTokenParseException;
}
