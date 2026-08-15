package com.example.demo.mapper;

import com.example.demo.entity.JStudentGroupHistory;
import com.example.demo.model.StudentGroupHistoryRecord;

public class StudentGroupHistoryMapper {
  public static StudentGroupHistoryRecord toRecord(JStudentGroupHistory studentGroupHistory) {
    return new StudentGroupHistoryRecord(
        studentGroupHistory.getId(),
        studentGroupHistory.getStartDate(),
        studentGroupHistory.getEndDate(),
        studentGroupHistory.getStudent() != null ? studentGroupHistory.getStudent().getId() : null,
        studentGroupHistory.getGroup() != null ? studentGroupHistory.getGroup().getId() : null);
  }
}
