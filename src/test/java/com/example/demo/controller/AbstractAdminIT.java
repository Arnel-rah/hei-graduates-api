package com.example.demo.controller;

import com.example.demo.conf.FacadeIT;
import com.example.demo.entity.JAccount;
import com.example.demo.model.Role;
import com.example.demo.repository.AccountRepository;
import com.example.demo.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

public abstract class AbstractAdminIT extends FacadeIT {

  @Autowired protected AccountRepository accountRepository;

  @Autowired protected JwtService jwtService;

  @Autowired protected PasswordEncoder passwordEncoder;

  protected String adminToken;

  @BeforeEach
  void setUpAdmin() {
    JAccount admin =
        JAccount.builder()
            .email("admin-test-" + System.nanoTime() + "@hei.mg")
            .password(passwordEncoder.encode("admin123"))
            .role(Role.ADMIN)
            .build();
    accountRepository.save(admin);
    adminToken = jwtService.generateToken(admin);
  }
}
