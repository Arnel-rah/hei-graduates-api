package com.example.demo.controller;

import com.example.demo.model.AuthToken;
import com.example.demo.model.LoginRequest;
import com.example.demo.model.RegisterRequest;
import com.example.demo.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<AuthToken> login(@RequestBody LoginRequest request) {
    AuthToken token = authService.login(request.email(), request.password());
    return ResponseEntity.ok(token);
  }

  @PostMapping("/register")
  public ResponseEntity<AuthToken> register(@RequestBody RegisterRequest request) {
    AuthToken token = authService.register(request.email(), request.password(), request.role());
    return ResponseEntity.status(HttpStatus.CREATED).body(token);
  }
}
