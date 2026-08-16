package com.example.demo.mapper;

import com.example.demo.entity.JAcademicYear;
import com.example.demo.model.AcademicYearRecord;

public class AcademicYearMapper {

  public static AcademicYearRecord toRecord(JAcademicYear academicYear) {
    return new AcademicYearRecord(
        academicYear.getId(),
        academicYear.getLabel(),
        academicYear.getOrder(),
        academicYear.getPromotion() != null ? academicYear.getPromotion().getId() : null,
        academicYear.isPublished());
  }
}
