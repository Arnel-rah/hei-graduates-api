package com.example.demo.service;

import com.example.demo.entity.JAccount;
import com.example.demo.entity.JTeacher;
import com.example.demo.mapper.TeacherMapper;
import com.example.demo.model.Role;
import com.example.demo.model.TeacherRecord;
import com.example.demo.model.TeacherSave;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeacherService {

  private final TeacherRepository teacherRepository;
  private final AccountRepository accountRepository;
  private final PasswordEncoder passwordEncoder;

  @Transactional
  public TeacherRecord create(TeacherSave request) {
    if (accountRepository.existsByEmail(request.email())) {
      throw new IllegalArgumentException("Email already used");
    }

    JAccount account =
        JAccount.builder()
            .email(request.email())
            .password(passwordEncoder.encode(request.password()))
            .role(Role.TEACHER)
            .build();
    accountRepository.save(account);

    JTeacher teacher = JTeacher.builder().name(request.name()).account(account).build();
    teacherRepository.save(teacher);

    return TeacherMapper.toRecord(teacher);
  }
}
