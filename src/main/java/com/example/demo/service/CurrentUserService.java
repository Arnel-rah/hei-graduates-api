package com.example.demo.service;

import com.example.demo.entity.JAccount;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrentUserService {

  private final AccountRepository accountRepository;

  public JAccount getCurrentAccount() {
    String email = SecurityContextHolder.getContext().getAuthentication().getName();

    return accountRepository
        .findByEmail(email)
        .orElseThrow(() -> new ResourceNotFoundException("Authenticated account not found"));
  }
}
