package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.entity.JAccount;
import com.example.demo.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
class JwtServiceTest {

  private JwtService jwtService;

  @BeforeEach
  void setUp() {
    jwtService = new JwtService("test-secret-key-256-bits-minimum-for-hs256-algorithm", 1);
  }

  @Test
  void generateToken_thenValidate_succeeds() {
    JAccount account =
        JAccount.builder().id("acc-1").email("nel@hei.mg").role(Role.STUDENT).build();

    String token = jwtService.generateToken(account);

    assertNotNull(token);
    assertTrue(jwtService.isTokenValid(token));
  }

  @Test
  void generateToken_thenExtractEmail_returnsCorrectEmail() {
    JAccount account =
        JAccount.builder().id("acc-1").email("nel@hei.mg").role(Role.STUDENT).build();

    String token = jwtService.generateToken(account);

    assertEquals("nel@hei.mg", jwtService.extractEmail(token));
  }

  @Test
  void generateToken_thenExtractRole_returnsCorrectRole() {
    JAccount account =
        JAccount.builder().id("acc-1").email("rakoto@hei.mg").role(Role.TEACHER).build();

    String token = jwtService.generateToken(account);

    assertEquals("TEACHER", jwtService.extractRole(token));
  }

  @Test
  void isTokenValid_withGarbageToken_returnsFalse() {
    assertFalse(jwtService.isTokenValid("not-a-real-token"));
  }

  @Test
  void isTokenValid_withTokenSignedByDifferentSecret_returnsFalse() {
    JwtService otherService =
        new JwtService("a-completely-different-secret-key-256-bits-minimum", 1);

    JAccount account =
        JAccount.builder().id("acc-1").email("nel@hei.mg").role(Role.STUDENT).build();

    String token = otherService.generateToken(account);

    assertFalse(jwtService.isTokenValid(token));
  }
}
