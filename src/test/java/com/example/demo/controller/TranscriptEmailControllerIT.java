package com.example.demo.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;

import com.example.demo.endpoint.event.EventProducer;
import com.example.demo.endpoint.event.model.TranscriptEmailRequested;
import com.example.demo.entity.JPromotion;
import com.example.demo.entity.JStudent;
import com.example.demo.model.Role;
import com.example.demo.model.TranscriptEmailRequest;
import com.example.demo.model.TranscriptEmailStatus;
import com.example.demo.repository.PromotionRepository;
import com.example.demo.repository.StudentRepository;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

class TranscriptEmailControllerIT extends AbstractAdminIT {

  @Autowired private TestRestTemplate restTemplate;

  @Autowired private PromotionRepository promotionRepository;

  @Autowired private StudentRepository studentRepository;

  @MockBean private EventProducer<TranscriptEmailRequested> eventProducer;

  private JStudent createStudent() {
    JPromotion promotion =
        JPromotion.builder()
            .label("Promotion " + System.nanoTime())
            .startYear(Instant.now())
            .build();
    promotionRepository.save(promotion);

    var account =
        com.example.demo.entity.JAccount.builder()
            .email("student-" + System.nanoTime() + "@hei.mg")
            .password(passwordEncoder.encode("password123"))
            .role(Role.STUDENT)
            .build();
    accountRepository.save(account);

    JStudent student =
        JStudent.builder()
            .name("Ralaivao")
            .firstName("NyLalaina")
            .ref("STU-" + System.nanoTime())
            .account(account)
            .promotion(promotion)
            .build();
    studentRepository.save(student);

    return student;
  }

  @Test
  void sendTranscriptByEmail_asAdmin_returns202AndProducesEvent() {
    JStudent student = createStudent();

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(adminToken);
    HttpEntity<Void> entity = new HttpEntity<>(headers);

    ResponseEntity<TranscriptEmailRequest> response =
        restTemplate.exchange(
            "/students/" + student.getId() + "/transcript-emails",
            HttpMethod.POST,
            entity,
            TranscriptEmailRequest.class);

    assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(student.getId(), response.getBody().studentId());
    assertEquals(TranscriptEmailStatus.PENDING, response.getBody().status());

    verify(eventProducer).accept(anyList());
  }

  @Test
  void sendTranscriptByEmail_asOtherStudent_returns403() {
    JStudent student = createStudent();

    var otherAccount =
        com.example.demo.entity.JAccount.builder()
            .email("other-" + System.nanoTime() + "@hei.mg")
            .password(passwordEncoder.encode("password123"))
            .role(Role.STUDENT)
            .build();
    accountRepository.save(otherAccount);
    String otherToken = jwtService.generateToken(otherAccount);

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(otherToken);
    HttpEntity<Void> entity = new HttpEntity<>(headers);

    ResponseEntity<String> response =
        restTemplate.exchange(
            "/students/" + student.getId() + "/transcript-emails",
            HttpMethod.POST,
            entity,
            String.class);

    assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
  }
}
