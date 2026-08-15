package com.example.demo.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.example.demo.entity.JAccount;
import com.example.demo.entity.JPromotion;
import com.example.demo.entity.JStudent;
import com.example.demo.model.StudentRecord;
import org.junit.jupiter.api.Test;

class StudentMapperTest {

  @Test
  void toRecord_mapsAllFieldsCorrectly() {
    JAccount account = JAccount.builder().id("acc-1").build();

    JPromotion promotion = JPromotion.builder().id("promo-1").build();

    JStudent student =
        JStudent.builder()
            .id("student-1")
            .name("Rahaingo")
            .firstName("Nel")
            .ref("STU001")
            .account(account)
            .promotion(promotion)
            .build();

    StudentRecord record = StudentMapper.toRecord(student);

    assertEquals("student-1", record.id());
    assertEquals("Rahaingo", record.name());
    assertEquals("Nel", record.firstName());
    assertEquals("STU001", record.ref());
    assertEquals("acc-1", record.accountId());
    assertEquals("promo-1", record.promotionId());
  }

  @Test
  void toRecord_handlesNullPromotion() {
    JAccount account = JAccount.builder().id("acc-2").build();

    JStudent student =
        JStudent.builder()
            .id("student-2")
            .name("Chen")
            .firstName("Yumei")
            .account(account)
            .promotion(null)
            .build();

    StudentRecord record = StudentMapper.toRecord(student);

    assertEquals("acc-2", record.accountId());
    assertNull(record.promotionId());
  }
}
