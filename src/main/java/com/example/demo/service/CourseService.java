package com.example.demo.service;

import com.example.demo.entity.JCourse;
import com.example.demo.mapper.CourseMapper;
import com.example.demo.model.CourseRecord;
import com.example.demo.model.CourseSave;
import com.example.demo.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseService {

  private final CourseRepository courseRepository;

  public CourseRecord create(CourseSave request) {
    JCourse course =
        JCourse.builder()
            .ref(request.ref())
            .title(request.title())
            .credits(request.credits())
            .mandatory(request.mandatory())
            .build();

    courseRepository.save(course);

    return CourseMapper.toRecord(course);
  }
}
