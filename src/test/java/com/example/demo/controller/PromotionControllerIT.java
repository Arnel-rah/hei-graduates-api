package com.example.demo.controller;

import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.entity.JPromotion;
import com.example.demo.model.PromotionRecord;
import com.example.demo.repository.PromotionRepository;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

class PromotionControllerIT extends AbstractAdminIT {

  @Autowired private TestRestTemplate restTemplate;

  @Autowired private PromotionRepository promotionRepository;

  @Test
  void getPromotions_returnsAllPromotions() {
    JPromotion promotion =
        JPromotion.builder()
            .label("Promotion Test " + System.nanoTime())
            .startYear(Instant.now())
            .build();
    promotionRepository.save(promotion);

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(adminToken);
    HttpEntity<Void> entity = new HttpEntity<>(headers);

    ResponseEntity<PromotionRecord[]> response =
        restTemplate.exchange("/promotions", HttpMethod.GET, entity, PromotionRecord[].class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());

    boolean found =
        java.util.Arrays.stream(response.getBody()).anyMatch(p -> p.id().equals(promotion.getId()));
    assertTrue(found, "The created promotion should appear in the list");
  }

  @Test
  void getPromotions_withoutToken_returnsUnauthorized() {
    ResponseEntity<String> response = restTemplate.getForEntity("/promotions", String.class);

    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
  }
}
