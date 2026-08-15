package com.example.demo.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.demo.entity.JAccount;
import com.example.demo.entity.JGrade;
import com.example.demo.entity.JGradeHistory;
import com.example.demo.model.GradeHistoryRecord;
import java.math.BigDecimal;
import java.time.Instant;
import org.junit.jupiter.api.Test;

class GradeHistoryMapperTest {

  @Test
  void toRecord_mapsAllFieldsCorrectly() {
    Instant modifiedAt = Instant.parse("2026-12-20T10:00:00Z");

    JGrade grade = JGrade.builder().id("grade-1").build();
    JAccount account = JAccount.builder().id("acc-1").build();

    JGradeHistory history =
        JGradeHistory.builder()
            .id("history-1")
            .oldValue(new BigDecimal("14.0"))
            .newValue(new BigDecimal("15.5"))
            .reason("Reclamation etudiant")
            .modifiedAt(modifiedAt)
            .grade(grade)
            .modifiedBy(account)
            .build();

    GradeHistoryRecord record = GradeHistoryMapper.toRecord(history);

    assertEquals("history-1", record.id());
    assertEquals(new BigDecimal("14.0"), record.oldValue());
    assertEquals(new BigDecimal("15.5"), record.newValue());
    assertEquals("Reclamation etudiant", record.reason());
    assertEquals(modifiedAt, record.modifiedAt());
    assertEquals("grade-1", record.gradeId());
    assertEquals("acc-1", record.modifiedByAccountId());
  }
}
