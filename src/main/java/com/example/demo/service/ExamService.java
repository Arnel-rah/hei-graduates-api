package com.example.demo.service;

import com.example.demo.entity.JCourseAssignment;
import com.example.demo.entity.JExam;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.ExamMapper;
import com.example.demo.model.ExamRecord;
import com.example.demo.model.ExamSave;
import com.example.demo.repository.CourseAssignmentRepository;
import com.example.demo.repository.ExamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExamService {

  private final ExamRepository examRepository;
  private final CourseAssignmentRepository courseAssignmentRepository;

  public ExamRecord create(String courseAssignmentId, ExamSave request) {
    JCourseAssignment courseAssignment =
        courseAssignmentRepository
            .findById(courseAssignmentId)
            .orElseThrow(() -> new ResourceNotFoundException("Course assignment not found"));

    JExam exam =
        JExam.builder()
            .dateExam(request.dateExam())
            .coefficient(request.coefficient())
            .courseAssignment(courseAssignment)
            .build();

    examRepository.save(exam);

    return ExamMapper.toRecord(exam);
  }
}
