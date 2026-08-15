package com.example.demo.mapper;

import com.example.demo.entity.JSemester;
import com.example.demo.model.SemesterRecord;

public class SemesterMapper {

  public static SemesterRecord toRecord(JSemester semester) {
    return new SemesterRecord(
        semester.getId(),
        semester.getLabel(),
        semester.getOrder(),
        semester.getPromotion() != null ? semester.getPromotion().getId() : null);
  }
}
