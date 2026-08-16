package com.example.demo.service;

import com.example.demo.entity.JAcademicYear;
import com.example.demo.entity.JSemester;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.SemesterMapper;
import com.example.demo.model.SemesterRecord;
import com.example.demo.model.SemesterSave;
import com.example.demo.repository.AcademicYearRepository;
import com.example.demo.repository.SemesterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SemesterService {

  private final SemesterRepository semesterRepository;
  private final AcademicYearRepository academicYearRepository;

  public SemesterRecord create(SemesterSave request) {
    JAcademicYear academicYear =
        academicYearRepository
            .findById(request.academicYearId())
            .orElseThrow(() -> new ResourceNotFoundException("Academic year not found"));

    JSemester semester =
        JSemester.builder()
            .label(request.label())
            .order(request.order())
            .promotion(academicYear.getPromotion())
            .academicYear(academicYear)
            .build();

    semesterRepository.save(semester);

    return SemesterMapper.toRecord(semester);
  }
}
