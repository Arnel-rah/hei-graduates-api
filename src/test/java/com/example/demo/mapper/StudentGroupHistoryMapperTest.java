package com.example.demo.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.example.demo.entity.JGroup;
import com.example.demo.entity.JStudent;
import com.example.demo.entity.JStudentGroupHistory;
import com.example.demo.model.StudentGroupHistoryRecord;
import java.time.Instant;
import org.junit.jupiter.api.Test;

class StudentGroupHistoryMapperTest {

  @Test
  void toRecord_mapsAllFieldsCorrectly() {
    Instant startDate = Instant.parse("2026-09-01T00:00:00Z");

    JStudent student = JStudent.builder().id("student-1").build();
    JGroup group = JGroup.builder().id("group-1").build();

    JStudentGroupHistory history =
        JStudentGroupHistory.builder()
            .id("sgh-1")
            .startDate(startDate)
            .endDate(null)
            .student(student)
            .group(group)
            .build();

    StudentGroupHistoryRecord record = StudentGroupHistoryMapper.toRecord(history);

    assertEquals("sgh-1", record.id());
    assertEquals(startDate, record.startDate());
    assertNull(record.endDate());
    assertEquals("student-1", record.studentId());
    assertEquals("group-1", record.groupId());
  }
}
