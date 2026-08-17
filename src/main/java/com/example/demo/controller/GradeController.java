package com.example.demo.controller;

import com.example.demo.model.GradeRecord;
import com.example.demo.model.GradeSave;
import com.example.demo.service.GradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class GradeController {

  private final GradeService gradeService;

  @PutMapping("/exams/{examId}/grades/{studentId}")
  public GradeRecord setStudentExamGrade(
      @PathVariable String examId, @PathVariable String studentId, @RequestBody GradeSave request) {
    return gradeService.setStudentExamGrade(examId, studentId, request);
  }
}
