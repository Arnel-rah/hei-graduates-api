package com.example.demo.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.demo.entity.JExam;
import com.example.demo.entity.JGrade;
import com.example.demo.entity.JStudent;
import com.example.demo.model.GradeRecord;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class GradeMapperTest {

  @Test
  void toRecord_mapsAllFieldsCorrectly() {
    JStudent student = JStudent.builder().id("student-1").build();
    JExam exam = JExam.builder().id("exam-1").build();

    JGrade grade =
        JGrade.builder()
            .id("grade-1")
            .value(new BigDecimal("15.5"))
            .student(student)
            .exam(exam)
            .build();

    GradeRecord record = GradeMapper.toRecord(grade);

    assertEquals("grade-1", record.id());
    assertEquals(new BigDecimal("15.5"), record.value());
    assertEquals("student-1", record.studentId());
    assertEquals("exam-1", record.examId());
  }
}
