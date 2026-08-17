package com.example.demo.controller;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.TranscriptRecord;
import com.example.demo.repository.StudentRepository;
import com.example.demo.service.AcademicResultService;
import com.example.demo.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TranscriptController {

  private final AcademicResultService academicResultService;
  private final StudentRepository studentRepository;
  private final StudentService studentService;

  @GetMapping("/students/{studentId}/transcript")
  public TranscriptRecord getStudentTranscript(@PathVariable String studentId) {
    var student =
        studentRepository
            .findById(studentId)
            .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

    studentService.checkStudentOwnership(student);

    return academicResultService.computeTranscript(studentId);
  }
}
