package com.example.demo.controller;

import com.example.demo.model.TeacherRecord;
import com.example.demo.model.TeacherSave;
import com.example.demo.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/teachers")
@RequiredArgsConstructor
public class TeacherController {

  private final TeacherService teacherService;

  @PostMapping
  public ResponseEntity<TeacherRecord> createTeacher(@RequestBody TeacherSave request) {
    TeacherRecord teacher = teacherService.create(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(teacher);
  }
}
