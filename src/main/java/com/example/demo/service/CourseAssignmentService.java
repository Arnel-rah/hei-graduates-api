package com.example.demo.service;

import com.example.demo.entity.JCourse;
import com.example.demo.entity.JCourseAssignment;
import com.example.demo.entity.JGroup;
import com.example.demo.entity.JSemester;
import com.example.demo.entity.JTeacher;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.CourseAssignmentMapper;
import com.example.demo.model.CourseAssignmentRecord;
import com.example.demo.model.CourseAssignmentSave;
import com.example.demo.repository.CourseAssignmentRepository;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.GroupRepository;
import com.example.demo.repository.SemesterRepository;
import com.example.demo.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseAssignmentService {

  private final CourseAssignmentRepository courseAssignmentRepository;
  private final CourseRepository courseRepository;
  private final TeacherRepository teacherRepository;
  private final GroupRepository groupRepository;
  private final SemesterRepository semesterRepository;

  public CourseAssignmentRecord create(CourseAssignmentSave request) {
    JCourse course =
        courseRepository
            .findById(request.courseId())
            .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

    JTeacher teacher =
        teacherRepository
            .findById(request.teacherId())
            .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));

    JGroup group =
        groupRepository
            .findById(request.groupId())
            .orElseThrow(() -> new ResourceNotFoundException("Group not found"));

    JSemester semester =
        semesterRepository
            .findById(request.semesterId())
            .orElseThrow(() -> new ResourceNotFoundException("Semester not found"));

    JCourseAssignment assignment =
        JCourseAssignment.builder()
            .course(course)
            .teacher(teacher)
            .group(group)
            .semester(semester)
            .build();

    courseAssignmentRepository.save(assignment);

    return CourseAssignmentMapper.toRecord(assignment);
  }
}
