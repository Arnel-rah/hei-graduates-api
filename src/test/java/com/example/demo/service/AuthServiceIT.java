package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.conf.FacadeIT;
import com.example.demo.model.AuthToken;
import com.example.demo.model.Role;
import com.example.demo.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class AuthServiceIT extends FacadeIT {

  @Autowired private AuthService authService;

  @Autowired private AccountRepository accountRepository;

  @Test
  void register_thenLogin_succeeds() {
    AuthToken registerToken = authService.register("nel@hei.mg", "password123", Role.STUDENT);

    assertNotNull(registerToken.token());
    assertEquals(Role.STUDENT, registerToken.role());

    AuthToken loginToken = authService.login("nel@hei.mg", "password123");

    assertNotNull(loginToken.token());
    assertEquals(Role.STUDENT, loginToken.role());
  }

  @Test
  void register_withAdminRole_throwsException() {
    assertThrows(
        IllegalArgumentException.class,
        () -> authService.register("admin@hei.mg", "password123", Role.ADMIN));
  }

  @Test
  void register_withExistingEmail_throwsException() {
    authService.register("ny@hei.mg", "password123", Role.STUDENT);

    assertThrows(
        IllegalArgumentException.class,
        () -> authService.register("ny@hei.mg", "otherpassword", Role.TEACHER));
  }

  @Test
  void login_withWrongPassword_throwsException() {
    authService.register("rakoto@hei.mg", "password123", Role.TEACHER);

    assertThrows(
        BadCredentialsException.class, () -> authService.login("rakoto@hei.mg", "wrongpassword"));
  }

  @Test
  void login_withUnknownEmail_throwsException() {
    assertThrows(
        BadCredentialsException.class, () -> authService.login("unknown@hei.mg", "password123"));
  }
}
