package com.example.demo.mapper;

import com.example.demo.entity.JStudent;
import com.example.demo.model.StudentRecord;

public class StudentMapper {
  public static StudentRecord toRecord(JStudent student) {
    return new StudentRecord(
        student.getId(),
        student.getName(),
        student.getFirstName(),
        student.getRef(),
        student.getAccount() != null ? student.getAccount().getId() : null,
        student.getPromotion() != null ? student.getPromotion().getId() : null);
  }
}
