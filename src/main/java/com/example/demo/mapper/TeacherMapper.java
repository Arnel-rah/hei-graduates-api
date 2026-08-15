package com.example.demo.mapper;

import com.example.demo.entity.JTeacher;
import com.example.demo.model.TeacherRecord;

public class TeacherMapper {

  public static TeacherRecord toRecord(JTeacher teacher) {
    return new TeacherRecord(
        teacher.getId(),
        teacher.getName(),
        teacher.getAccount() != null ? teacher.getAccount().getId() : null);
  }
}
