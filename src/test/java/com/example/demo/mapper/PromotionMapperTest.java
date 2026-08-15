package com.example.demo.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.demo.entity.JPromotion;
import com.example.demo.model.PromotionRecord;
import java.time.Instant;
import org.junit.jupiter.api.Test;

class PromotionMapperTest {

  @Test
  void toRecord_mapsAllFieldsCorrectly() {
    Instant startYear = Instant.parse("2024-09-01T00:00:00Z");

    JPromotion promotion =
        JPromotion.builder()
            .id("promo-1")
            .label("Promotion 2024-2027")
            .startYear(startYear)
            .build();

    PromotionRecord record = PromotionMapper.toRecord(promotion);

    assertEquals("promo-1", record.id());
    assertEquals("Promotion 2024-2027", record.label());
    assertEquals(startYear, record.startYear());
  }
}
