package com.example.demo.controller;

import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.entity.JAcademicYear;
import com.example.demo.entity.JPromotion;
import com.example.demo.model.SemesterRecord;
import com.example.demo.model.SemesterSave;
import com.example.demo.repository.AcademicYearRepository;
import com.example.demo.repository.PromotionRepository;
import com.example.demo.repository.SemesterRepository;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

class SemesterControllerIT extends AbstractAdminIT {

  @Autowired private TestRestTemplate restTemplate;

  @Autowired private SemesterRepository semesterRepository;

  @Autowired private AcademicYearRepository academicYearRepository;

  @Autowired private PromotionRepository promotionRepository;

  @Test
  void createSemester_derivesPromotionFromAcademicYear() {
    JPromotion promotion =
        JPromotion.builder().label("Promotion Test").startYear(Instant.now()).build();
    promotionRepository.save(promotion);

    JAcademicYear academicYear =
        JAcademicYear.builder().label("L1").order(1).promotion(promotion).published(false).build();
    academicYearRepository.save(academicYear);

    SemesterSave request = new SemesterSave("S1", 1, academicYear.getId());

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(adminToken);
    HttpEntity<SemesterSave> entity = new HttpEntity<>(request, headers);

    ResponseEntity<SemesterRecord> response =
        restTemplate.postForEntity("/semesters", entity, SemesterRecord.class);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("S1", response.getBody().label());
    assertEquals(academicYear.getId(), response.getBody().academicYearId());
  }

  @Test
  void createSemester_withUnknownAcademicYear_returns404() {
    SemesterSave request = new SemesterSave("S1", 1, "unknown-id");

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(adminToken);
    HttpEntity<SemesterSave> entity = new HttpEntity<>(request, headers);

    ResponseEntity<String> response =
        restTemplate.postForEntity("/semesters", entity, String.class);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }
}
