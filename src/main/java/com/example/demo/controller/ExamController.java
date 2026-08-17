package com.example.demo.controller;

import com.example.demo.model.ExamRecord;
import com.example.demo.model.ExamSave;
import com.example.demo.service.ExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/course-assignments/{courseAssignmentId}/exams")
@RequiredArgsConstructor
public class ExamController {

  private final ExamService examService;

  @PostMapping
  public ResponseEntity<ExamRecord> createExam(
      @PathVariable String courseAssignmentId, @RequestBody ExamSave request) {
    ExamRecord exam = examService.create(courseAssignmentId, request);
    return ResponseEntity.status(HttpStatus.CREATED).body(exam);
  }
}
