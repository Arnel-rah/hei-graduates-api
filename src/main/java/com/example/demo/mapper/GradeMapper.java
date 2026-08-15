package com.example.demo.mapper;

import com.example.demo.entity.JGrade;
import com.example.demo.model.GradeRecord;

public class GradeMapper {
  public static GradeRecord toRecord(JGrade grade) {
    return new GradeRecord(
        grade.getId(),
        grade.getValue(),
        grade.getStudent() != null ? grade.getStudent().getId() : null,
        grade.getExam() != null ? grade.getExam().getId() : null);
  }
}
