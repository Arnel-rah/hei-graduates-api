package com.example.demo.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.demo.entity.JCourse;
import com.example.demo.model.CourseRecord;
import org.junit.jupiter.api.Test;

class CourseMapperTest {

  @Test
  void toRecord_mapsAllFieldsCorrectly() {
    JCourse course =
        JCourse.builder().id("course-1").ref("ALGO101").title("Algorithmique").credits(5).build();

    CourseRecord record = CourseMapper.toRecord(course);

    assertEquals("course-1", record.id());
    assertEquals("ALGO101", record.ref());
    assertEquals("Algorithmique", record.title());
    assertEquals(5, record.credits());
  }
}
