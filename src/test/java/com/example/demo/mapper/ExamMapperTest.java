package com.example.demo.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.demo.entity.JCourseAssignment;
import com.example.demo.entity.JExam;
import com.example.demo.model.ExamRecord;
import java.math.BigDecimal;
import java.time.Instant;
import org.junit.jupiter.api.Test;

class ExamMapperTest {

  @Test
  void toRecord_mapsAllFieldsCorrectly() {
    Instant dateExam = Instant.parse("2026-12-15T09:00:00Z");

    JCourseAssignment assignment = JCourseAssignment.builder().id("assign-1").build();

    JExam exam =
        JExam.builder()
            .id("exam-1")
            .dateExam(dateExam)
            .coefficient(new BigDecimal("2.0"))
            .courseAssignment(assignment)
            .build();

    ExamRecord record = ExamMapper.toRecord(exam);

    assertEquals("exam-1", record.id());
    assertEquals(dateExam, record.dateExam());
    assertEquals(new BigDecimal("2.0"), record.coefficient());
    assertEquals("assign-1", record.courseAssignmentId());
  }
}
