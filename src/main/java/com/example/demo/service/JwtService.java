package com.example.demo.service;

import com.example.demo.entity.JAccount;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtService {

  private final SecretKey key;
  private final long expirationDays;

  public JwtService(
      @Value("${jwt.secret}") String secret, @Value("${jwt.expiration-days}") long expirationDays) {
    this.key = Keys.hmacShaKeyFor(secret.getBytes());
    this.expirationDays = expirationDays;
  }

  public String generateToken(JAccount account) {
    return Jwts.builder()
        .subject(account.getEmail())
        .claim("role", account.getRole().name())
        .issuedAt(new Date())
        .expiration(Date.from(Instant.now().plus(expirationDays, ChronoUnit.DAYS)))
        .signWith(key)
        .compact();
  }

  public boolean isTokenValid(String token) {
    try {
      parseClaims(token);
      return true;
    } catch (JwtException | IllegalArgumentException e) {
      return false;
    }
  }

  public String extractEmail(String token) {
    return parseClaims(token).getSubject();
  }

  public String extractRole(String token) {
    return parseClaims(token).get("role", String.class);
  }

  private Claims parseClaims(String token) {
    return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
  }
}
