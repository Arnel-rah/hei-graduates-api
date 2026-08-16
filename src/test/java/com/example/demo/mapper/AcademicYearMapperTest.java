package com.example.demo.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.example.demo.entity.JAcademicYear;
import com.example.demo.entity.JPromotion;
import com.example.demo.model.AcademicYearRecord;
import org.junit.jupiter.api.Test;

class AcademicYearMapperTest {

  @Test
  void toRecord_mapsAllFieldsCorrectly() {
    JPromotion promotion = JPromotion.builder().id("promo-4").build();

    JAcademicYear academicYear =
        JAcademicYear.builder()
            .id("ay-1")
            .label("L1")
            .order(1)
            .promotion(promotion)
            .published(true)
            .build();

    AcademicYearRecord record = AcademicYearMapper.toRecord(academicYear);

    assertEquals("ay-1", record.id());
    assertEquals("L1", record.label());
    assertEquals(1, record.order());
    assertEquals("promo-4", record.promotionId());
    assertEquals(true, record.published());
  }

  @Test
  void toRecord_handlesNullPromotion() {
    JAcademicYear academicYear =
        JAcademicYear.builder()
            .id("ay-2")
            .label("L2")
            .order(2)
            .promotion(null)
            .published(false)
            .build();

    AcademicYearRecord record = AcademicYearMapper.toRecord(academicYear);

    assertNull(record.promotionId());
    assertEquals(false, record.published());
  }
}
