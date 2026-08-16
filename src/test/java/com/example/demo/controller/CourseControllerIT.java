package com.example.demo.controller;

import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.model.CourseRecord;
import com.example.demo.model.CourseSave;
import com.example.demo.repository.CourseRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

class CourseControllerIT extends AbstractAdminIT {

  @Autowired private TestRestTemplate restTemplate;

  @Autowired private CourseRepository courseRepository;

  @Test
  void createCourse_asAdmin_succeeds() {
    CourseSave request = new CourseSave("ALGO101", "Algorithmique", 5, true);

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(adminToken);
    HttpEntity<CourseSave> entity = new HttpEntity<>(request, headers);

    ResponseEntity<CourseRecord> response =
        restTemplate.postForEntity("/courses", entity, CourseRecord.class);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("ALGO101", response.getBody().ref());
    assertEquals(5, response.getBody().credits());
    assertTrue(response.getBody().mandatory());
  }

  @Test
  void createCourse_withoutToken_returnsUnauthorized() {
    CourseSave request = new CourseSave("RES401", "Reseaux", 4, false);

    ResponseEntity<String> response = restTemplate.postForEntity("/courses", request, String.class);

    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
  }
}
