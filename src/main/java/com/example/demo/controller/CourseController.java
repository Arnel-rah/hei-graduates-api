package com.example.demo.controller;

import com.example.demo.model.CourseRecord;
import com.example.demo.model.CourseSave;
import com.example.demo.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {

  private final CourseService courseService;

  @PostMapping
  public ResponseEntity<CourseRecord> createCourse(@RequestBody CourseSave request) {
    CourseRecord course = courseService.create(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(course);
  }
}
