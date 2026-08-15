package com.example.demo.controller;

import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.conf.FacadeIT;
import com.example.demo.model.AuthToken;
import com.example.demo.model.LoginRequest;
import com.example.demo.model.RegisterRequest;
import com.example.demo.model.Role;
import com.example.demo.repository.AccountRepository;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ActiveProfiles("test")
class AuthControllerIT extends FacadeIT {

  @Autowired private TestRestTemplate restTemplate;

  @Autowired private AccountRepository accountRepository;

  private String uniqueEmail(String prefix) {
    return prefix + "-" + UUID.randomUUID() + "@hei.mg";
  }

  @Test
  void register_thenLogin_fullFlow_succeeds() {
    String email = uniqueEmail("nel");
    RegisterRequest registerRequest =
        new RegisterRequest(email, "password123", Role.STUDENT, "Rahaingo", "Nel", "STU001");

    ResponseEntity<AuthToken> registerResponse =
        restTemplate.postForEntity("/register", registerRequest, AuthToken.class);

    assertEquals(HttpStatus.CREATED, registerResponse.getStatusCode());
    assertNotNull(registerResponse.getBody());
    assertNotNull(registerResponse.getBody().token());
    assertEquals(Role.STUDENT, registerResponse.getBody().role());

    LoginRequest loginRequest = new LoginRequest(email, "password123");

    ResponseEntity<AuthToken> loginResponse =
        restTemplate.postForEntity("/login", loginRequest, AuthToken.class);

    assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
    assertNotNull(loginResponse.getBody());
    assertNotNull(loginResponse.getBody().token());
  }

  @Test
  void register_withAdminRole_isRejected() {
    RegisterRequest request =
        new RegisterRequest(uniqueEmail("admin"), "password123", Role.ADMIN, "Admin", "Root", null);

    ResponseEntity<String> response =
        restTemplate.postForEntity("/register", request, String.class);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  void login_withWrongPassword_returnsUnauthorized() {
    String email = uniqueEmail("rasoa.controller");
    RegisterRequest registerRequest =
        new RegisterRequest(email, "password123", Role.TEACHER, "Rasoa", "Jean", null);
    restTemplate.postForEntity("/register", registerRequest, AuthToken.class);

    LoginRequest loginRequest = new LoginRequest(email, "wrongpassword");

    ResponseEntity<String> response =
        restTemplate.postForEntity("/login", loginRequest, String.class);

    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
  }

  @Test
  void protectedEndpoint_withoutToken_returnsUnauthorized() {
    ResponseEntity<String> response = restTemplate.getForEntity("/promotions", String.class);

    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
  }
}
