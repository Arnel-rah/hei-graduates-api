package com.example.demo.controller;

import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.entity.JPromotion;
import com.example.demo.model.AcademicYearRecord;
import com.example.demo.model.AcademicYearSave;
import com.example.demo.repository.AcademicYearRepository;
import com.example.demo.repository.PromotionRepository;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

class AcademicYearControllerIT extends AbstractAdminIT {

  @Autowired private TestRestTemplate restTemplate;

  @Autowired private AcademicYearRepository academicYearRepository;

  @Autowired private PromotionRepository promotionRepository;

  private String promotionId;

  private String createPromotion() {
    JPromotion promotion =
        JPromotion.builder().label("Promotion Test").startYear(Instant.now()).build();
    promotionRepository.save(promotion);
    return promotion.getId();
  }

  @Test
  void createAcademicYear_asAdmin_succeeds() {
    promotionId = createPromotion();

    AcademicYearSave request = new AcademicYearSave("L1", 1, promotionId);

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(adminToken);
    HttpEntity<AcademicYearSave> entity = new HttpEntity<>(request, headers);

    ResponseEntity<AcademicYearRecord> response =
        restTemplate.postForEntity("/academic-years", entity, AcademicYearRecord.class);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("L1", response.getBody().label());
    assertFalse(response.getBody().published());
  }

  @Test
  void createAcademicYear_withUnknownPromotion_returns404() {
    AcademicYearSave request = new AcademicYearSave("L1", 1, "unknown-id");

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(adminToken);
    HttpEntity<AcademicYearSave> entity = new HttpEntity<>(request, headers);

    ResponseEntity<String> response =
        restTemplate.postForEntity("/academic-years", entity, String.class);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  void listAcademicYearsOfPromotion_returnsCreatedYear() {
    promotionId = createPromotion();

    AcademicYearSave request = new AcademicYearSave("L2", 2, promotionId);
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(adminToken);
    ResponseEntity<AcademicYearRecord> createResponse =
        restTemplate.postForEntity(
            "/academic-years", new HttpEntity<>(request, headers), AcademicYearRecord.class);
    String createdId = createResponse.getBody().id();

    HttpEntity<Void> getEntity = new HttpEntity<>(headers);
    ResponseEntity<AcademicYearRecord[]> response =
        restTemplate.exchange(
            "/promotions/" + promotionId + "/academic-years",
            HttpMethod.GET,
            getEntity,
            AcademicYearRecord[].class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());

    boolean found =
        java.util.Arrays.stream(response.getBody())
            .anyMatch(ay -> ay.id().equals(createdId) && ay.label().equals("L2"));
    assertTrue(found, "The created academic year should appear in the list of THIS promotion");
  }

  @Test
  void publishAcademicYear_setsPublishedTrue() {
    promotionId = createPromotion();

    AcademicYearSave request = new AcademicYearSave("L3", 3, promotionId);
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(adminToken);
    ResponseEntity<AcademicYearRecord> createResponse =
        restTemplate.postForEntity(
            "/academic-years", new HttpEntity<>(request, headers), AcademicYearRecord.class);

    String academicYearId = createResponse.getBody().id();

    HttpEntity<Void> publishEntity = new HttpEntity<>(headers);
    ResponseEntity<AcademicYearRecord> publishResponse =
        restTemplate.exchange(
            "/academic-years/" + academicYearId + "/publish",
            HttpMethod.POST,
            publishEntity,
            AcademicYearRecord.class);

    assertEquals(HttpStatus.OK, publishResponse.getStatusCode());
    assertTrue(publishResponse.getBody().published());
  }
}
