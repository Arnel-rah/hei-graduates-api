package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.conf.FacadeIT;
import com.example.demo.model.AuthToken;
import com.example.demo.model.Role;
import com.example.demo.repository.AccountRepository;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ActiveProfiles("test")
class AuthServiceIT extends FacadeIT {

  @Autowired private AuthService authService;

  @Autowired private AccountRepository accountRepository;

  private String uniqueEmail(String prefix) {
    return prefix + "-" + UUID.randomUUID() + "@hei.mg";
  }

  @Test
  void register_thenLogin_succeeds() {
    String email = uniqueEmail("nel");
    AuthToken registerToken = authService.register(email, "password123", Role.STUDENT);

    assertNotNull(registerToken.token());
    assertEquals(Role.STUDENT, registerToken.role());

    AuthToken loginToken = authService.login(email, "password123");

    assertNotNull(loginToken.token());
    assertEquals(Role.STUDENT, loginToken.role());
  }

  @Test
  void register_withAdminRole_throwsException() {
    assertThrows(
        IllegalArgumentException.class,
        () -> authService.register(uniqueEmail("admin"), "password123", Role.ADMIN));
  }

  @Test
  void register_withExistingEmail_throwsException() {
    String email = uniqueEmail("ny");
    authService.register(email, "password123", Role.STUDENT);

    assertThrows(
        IllegalArgumentException.class,
        () -> authService.register(email, "otherpassword", Role.TEACHER));
  }

  @Test
  void login_withWrongPassword_throwsException() {
    String email = uniqueEmail("rakoto");
    authService.register(email, "password123", Role.TEACHER);

    assertThrows(BadCredentialsException.class, () -> authService.login(email, "wrongpassword"));
  }

  @Test
  void login_withUnknownEmail_throwsException() {
    assertThrows(
        BadCredentialsException.class,
        () -> authService.login(uniqueEmail("unknown"), "password123"));
  }
}
