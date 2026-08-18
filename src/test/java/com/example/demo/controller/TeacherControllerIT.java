package com.example.demo.controller;

import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.model.TeacherRecord;
import com.example.demo.model.TeacherSave;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

class TeacherControllerIT extends AbstractAdminIT {

  @Autowired private TestRestTemplate restTemplate;

  @Test
  void createTeacher_asAdmin_createsAccountAndTeacher() {
    TeacherSave request =
        new TeacherSave("Rasoa", "rasoa-" + System.nanoTime() + "@hei.mg", "password123");

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(adminToken);
    HttpEntity<TeacherSave> entity = new HttpEntity<>(request, headers);

    ResponseEntity<TeacherRecord> response =
        restTemplate.postForEntity("/teachers", entity, TeacherRecord.class);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("Rasoa", response.getBody().name());
    assertNotNull(response.getBody().accountId());
  }

  @Test
  void createTeacher_withDuplicateEmail_returns400() {
    String email = "duplicate-teacher-" + System.nanoTime() + "@hei.mg";
    TeacherSave first = new TeacherSave("Rasoa", email, "password123");
    TeacherSave second = new TeacherSave("Rakoto", email, "password123");

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(adminToken);

    restTemplate.postForEntity("/teachers", new HttpEntity<>(first, headers), TeacherRecord.class);
    ResponseEntity<String> response =
        restTemplate.postForEntity("/teachers", new HttpEntity<>(second, headers), String.class);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  void createTeacher_withoutToken_returnsUnauthorized() {
    TeacherSave request = new TeacherSave("Rasoa", "any@hei.mg", "password123");

    ResponseEntity<String> response =
        restTemplate.postForEntity("/teachers", request, String.class);

    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
  }
}
