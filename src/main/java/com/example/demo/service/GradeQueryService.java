package com.example.demo.service;

import com.example.demo.entity.JStudent;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.GradeMapper;
import com.example.demo.model.GradeRecord;
import com.example.demo.repository.GradeRepository;
import com.example.demo.repository.StudentRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GradeQueryService {

  private final GradeRepository gradeRepository;
  private final StudentRepository studentRepository;
  private final StudentService studentService;

  public List<GradeRecord> getStudentGrades(String studentId) {
    JStudent student =
        studentRepository
            .findById(studentId)
            .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

    studentService.checkStudentOwnership(student);

    return gradeRepository.findByStudentId(studentId).stream().map(GradeMapper::toRecord).toList();
  }
}
