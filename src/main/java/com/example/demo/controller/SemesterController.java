package com.example.demo.controller;

import com.example.demo.model.SemesterRecord;
import com.example.demo.model.SemesterSave;
import com.example.demo.service.SemesterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/semesters")
@RequiredArgsConstructor
public class SemesterController {

  private final SemesterService semesterService;

  @PostMapping
  public ResponseEntity<SemesterRecord> createSemester(@RequestBody SemesterSave request) {
    SemesterRecord semester = semesterService.create(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(semester);
  }
}
