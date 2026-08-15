package com.example.demo.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.demo.entity.JPromotion;
import com.example.demo.entity.JSemester;
import com.example.demo.model.SemesterRecord;
import org.junit.jupiter.api.Test;

class SemesterMapperTest {

  @Test
  void toRecord_mapsAllFieldsCorrectly() {
    JPromotion promotion = JPromotion.builder().id("promo-4").build();

    JSemester semester =
        JSemester.builder().id("sem-1").label("S4").order(4).promotion(promotion).build();

    SemesterRecord record = SemesterMapper.toRecord(semester);

    assertEquals("sem-1", record.id());
    assertEquals("S4", record.label());
    assertEquals(4, record.order());
    assertEquals("promo-4", record.promotionId());
  }
}
