package com.example.demo.service;

import com.example.demo.entity.JAccount;
import com.example.demo.model.AuthToken;
import com.example.demo.model.Role;
import com.example.demo.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final AccountRepository accountRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;

  public AuthToken login(String email, String rawPassword) {
    JAccount account =
        accountRepository
            .findByEmail(email)
            .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

    if (!passwordEncoder.matches(rawPassword, account.getPassword())) {
      throw new BadCredentialsException("Invalid credentials");
    }

    String token = jwtService.generateToken(account);
    return new AuthToken(token, account.getRole());
  }

  public AuthToken register(String email, String rawPassword, Role role) {
    if (role == Role.ADMIN) {
      throw new IllegalArgumentException("Self-registration as ADMIN is not allowed");
    }

    if (accountRepository.existsByEmail(email)) {
      throw new IllegalArgumentException("Email already used");
    }

    JAccount account =
        JAccount.builder()
            .email(email)
            .password(passwordEncoder.encode(rawPassword))
            .role(role)
            .build();

    accountRepository.save(account);

    String token = jwtService.generateToken(account);
    return new AuthToken(token, role);
  }
}
