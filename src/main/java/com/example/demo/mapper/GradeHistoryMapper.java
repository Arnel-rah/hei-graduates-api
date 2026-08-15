package com.example.demo.mapper;

import com.example.demo.entity.JGradeHistory;
import com.example.demo.model.GradeHistoryRecord;

public class GradeHistoryMapper {
  public static GradeHistoryRecord toRecord(JGradeHistory gradeHistory) {
    return new GradeHistoryRecord(
        gradeHistory.getId(),
        gradeHistory.getOldValue(),
        gradeHistory.getNewValue(),
        gradeHistory.getReason(),
        gradeHistory.getModifiedAt(),
        gradeHistory.getGrade() != null ? gradeHistory.getGrade().getId() : null,
        gradeHistory.getModifiedBy() != null ? gradeHistory.getModifiedBy().getId() : null);
  }
}
