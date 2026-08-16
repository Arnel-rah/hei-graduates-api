package com.example.demo.mapper;

import com.example.demo.entity.JCourse;
import com.example.demo.model.CourseRecord;

public class CourseMapper {

  public static CourseRecord toRecord(JCourse course) {
    return new CourseRecord(
        course.getId(),
        course.getRef(),
        course.getTitle(),
        course.getCredits(),
        course.isMandatory());
  }
}
