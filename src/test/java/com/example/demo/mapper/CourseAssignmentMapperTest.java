package com.example.demo.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.example.demo.entity.*;
import com.example.demo.model.CourseAssignmentRecord;
import org.junit.jupiter.api.Test;

class CourseAssignmentMapperTest {

  @Test
  void toRecord_mapsAllFieldsCorrectly() {
    JCourse course = JCourse.builder().id("course-1").build();
    JTeacher teacher = JTeacher.builder().id("teacher-1").build();
    JGroup group = JGroup.builder().id("group-1").build();
    JSemester semester = JSemester.builder().id("sem-1").build();

    JCourseAssignment assignment =
        JCourseAssignment.builder()
            .id("assign-1")
            .course(course)
            .teacher(teacher)
            .group(group)
            .semester(semester)
            .build();

    CourseAssignmentRecord record = CourseAssignmentMapper.toRecord(assignment);

    assertEquals("assign-1", record.id());
    assertEquals("course-1", record.courseId());
    assertEquals("teacher-1", record.teacherId());
    assertEquals("group-1", record.groupId());
    assertEquals("sem-1", record.semesterId());
  }

  @Test
  void toRecord_handlesNullRelations() {
    JCourseAssignment assignment = JCourseAssignment.builder().id("assign-2").build();

    CourseAssignmentRecord record = CourseAssignmentMapper.toRecord(assignment);

    assertNull(record.courseId());
    assertNull(record.teacherId());
    assertNull(record.groupId());
    assertNull(record.semesterId());
  }
}
