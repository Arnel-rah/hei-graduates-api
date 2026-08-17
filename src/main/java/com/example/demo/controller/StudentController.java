package com.example.demo.controller;

import com.example.demo.model.GradeRecord;
import com.example.demo.model.StudentRecord;
import com.example.demo.model.StudentSave;
import com.example.demo.service.GradeQueryService;
import com.example.demo.service.StudentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

  private final StudentService studentService;
  private final GradeQueryService gradeQueryService;

  @PostMapping
  public ResponseEntity<StudentRecord> createStudent(@RequestBody StudentSave request) {
    StudentRecord student = studentService.create(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(student);
  }

  @GetMapping("/{studentId}")
  public ResponseEntity<StudentRecord> getStudentById(@PathVariable String studentId) {
    return ResponseEntity.ok(studentService.getById(studentId));
  }

  @GetMapping("/{studentId}/grades")
  public ResponseEntity<List<GradeRecord>> getStudentGrades(@PathVariable String studentId) {
    return ResponseEntity.ok(gradeQueryService.getStudentGrades(studentId));
  }
}
