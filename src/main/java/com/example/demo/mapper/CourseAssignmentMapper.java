package com.example.demo.mapper;

import com.example.demo.entity.JCourseAssignment;
import com.example.demo.model.CourseAssignmentRecord;

public class CourseAssignmentMapper {
  public static CourseAssignmentRecord toRecord(JCourseAssignment courseAssignment) {
    return new CourseAssignmentRecord(
        courseAssignment.getId(),
        courseAssignment.getCourse() != null ? courseAssignment.getCourse().getId() : null,
        courseAssignment.getTeacher() != null ? courseAssignment.getTeacher().getId() : null,
        courseAssignment.getGroup() != null ? courseAssignment.getGroup().getId() : null,
        courseAssignment.getSemester() != null ? courseAssignment.getSemester().getId() : null);
  }
}
