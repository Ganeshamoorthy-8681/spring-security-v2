package com.spring.security.component;

import com.spring.security.exceptions.JwtTokenGenerationFailedException;
import com.spring.security.exceptions.JwtTokenParseException;
import com.spring.security.exceptions.PreconditionViolationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtTokenGeneratorImpl implements JwtTokenGenerator {

  @Value("${jwt.secret.key}")
  private String secretKey;

  /**
   * Generate JWT token based on email and roles.
   *
   * @return a JWT token as a String
   */
  @Override
  public String generate(Map<String, Object> claims) throws JwtTokenGenerationFailedException {

    try {
        return Jwts.builder()
                .setClaims(claims) // custom claim
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // expires in 1 hour
                .signWith(getSecretKey()) // sign the token
                .compact();
    } catch (Exception e) {
      log.error("Error generating JWT token: {}", e.getMessage());
      throw new JwtTokenGenerationFailedException("Failed to generate JWT token", e);
    }
  }

  /**
   * Get claims from the JWT token.
   *
   * @param token the JWT token as a String
   * @return Claims object containing the claims from the token
   */
  @Override
  public Claims getClaims(String token) throws JwtTokenParseException, PreconditionViolationException {

    //Need to Implement a common validation utility to check for null or empty strings.
    if (token == null || token.isEmpty()) {
      log.error("JWT token is null or empty");
      throw new PreconditionViolationException("JWT token cannot be null or empty");
    }

    try {
      return Jwts.parserBuilder()
              .setSigningKey(getSecretKey())
              .build()
              .parseClaimsJws(token)
              .getBody();
    } catch (Exception e) {
      log.error("Error parsing JWT token: {}", e.getMessage());
      throw new JwtTokenParseException("Unable to get the claims from the token", e);
    }

  }

  private Key getSecretKey() {
    // Convert the secret key string to a Key object
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
  }
}
