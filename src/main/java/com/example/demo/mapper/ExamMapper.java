package com.example.demo.mapper;

import com.example.demo.entity.JExam;
import com.example.demo.model.ExamRecord;

public class ExamMapper {
  public static ExamRecord toRecord(JExam exam) {
    return new ExamRecord(
        exam.getId(),
        exam.getDateExam(),
        exam.getCoefficient(),
        exam.getCourseAssignment() != null ? exam.getCourseAssignment().getId() : null);
  }
}
