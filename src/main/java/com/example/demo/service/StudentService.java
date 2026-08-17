package com.example.demo.service;

import com.example.demo.entity.JAccount;
import com.example.demo.entity.JPromotion;
import com.example.demo.entity.JStudent;
import com.example.demo.exception.ForbiddenOperationException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.StudentMapper;
import com.example.demo.model.Role;
import com.example.demo.model.StudentRecord;
import com.example.demo.model.StudentSave;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.PromotionRepository;
import com.example.demo.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudentService {

  private final StudentRepository studentRepository;
  private final AccountRepository accountRepository;
  private final PromotionRepository promotionRepository;
  private final PasswordEncoder passwordEncoder;
  private final CurrentUserService currentUserService;

  @Transactional
  public StudentRecord create(StudentSave request) {
    if (accountRepository.existsByEmail(request.email())) {
      throw new IllegalArgumentException("Email already used");
    }

    JAccount account =
        JAccount.builder()
            .email(request.email())
            .password(passwordEncoder.encode(request.password()))
            .role(Role.STUDENT)
            .build();
    accountRepository.save(account);

    JPromotion promotion = null;
    if (request.promotionId() != null) {
      promotion =
          promotionRepository
              .findById(request.promotionId())
              .orElseThrow(() -> new ResourceNotFoundException("Promotion not found"));
    }

    JStudent student =
        JStudent.builder()
            .name(request.name())
            .firstName(request.firstName())
            .ref(request.ref())
            .account(account)
            .promotion(promotion)
            .build();
    studentRepository.save(student);

    return StudentMapper.toRecord(student);
  }

  public StudentRecord getById(String studentId) {
    JStudent student =
        studentRepository
            .findById(studentId)
            .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

    checkStudentOwnership(student);

    return StudentMapper.toRecord(student);
  }

  public void checkStudentOwnership(JStudent student) {
    JAccount currentAccount = currentUserService.getCurrentAccount();

    boolean isOwnRecord = student.getAccount().getId().equals(currentAccount.getId());

    if (currentAccount.getRole() == Role.STUDENT && !isOwnRecord) {
      throw new ForbiddenOperationException("You can only access your own student record");
    }
  }
}
