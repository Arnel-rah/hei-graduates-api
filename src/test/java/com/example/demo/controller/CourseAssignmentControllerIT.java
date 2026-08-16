package com.example.demo.controller;

import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.entity.*;
import com.example.demo.model.CourseAssignmentRecord;
import com.example.demo.model.CourseAssignmentSave;
import com.example.demo.model.Role;
import com.example.demo.repository.*;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

class CourseAssignmentControllerIT extends AbstractAdminIT {

  @Autowired private TestRestTemplate restTemplate;

  @Autowired private CourseAssignmentRepository courseAssignmentRepository;

  @Autowired private CourseRepository courseRepository;

  @Autowired private TeacherRepository teacherRepository;

  @Autowired private GroupRepository groupRepository;

  @Autowired private SemesterRepository semesterRepository;

  @Autowired private PromotionRepository promotionRepository;

  @Test
  void createCourseAssignment_withValidRefs_succeeds() {
    JPromotion promotion =
        JPromotion.builder().label("Promotion Test").startYear(Instant.now()).build();
    promotionRepository.save(promotion);

    JCourse course =
        JCourse.builder().ref("RES401").title("Reseaux").credits(4).mandatory(true).build();
    courseRepository.save(course);

    JAccount teacherAccount =
        JAccount.builder()
            .email("teacher-" + System.nanoTime() + "@hei.mg")
            .password(passwordEncoder.encode("password123"))
            .role(Role.TEACHER)
            .build();
    accountRepository.save(teacherAccount);

    JTeacher teacher = JTeacher.builder().name("Rasoa").account(teacherAccount).build();
    teacherRepository.save(teacher);

    JGroup group = JGroup.builder().ref("TN").promotion(promotion).build();
    groupRepository.save(group);

    JSemester semester = JSemester.builder().label("S4").order(4).promotion(promotion).build();
    semesterRepository.save(semester);

    CourseAssignmentSave request =
        new CourseAssignmentSave(course.getId(), teacher.getId(), group.getId(), semester.getId());

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(adminToken);
    HttpEntity<CourseAssignmentSave> entity = new HttpEntity<>(request, headers);

    ResponseEntity<CourseAssignmentRecord> response =
        restTemplate.postForEntity("/course-assignments", entity, CourseAssignmentRecord.class);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(course.getId(), response.getBody().courseId());
    assertEquals(teacher.getId(), response.getBody().teacherId());
    assertEquals(group.getId(), response.getBody().groupId());
    assertEquals(semester.getId(), response.getBody().semesterId());
  }

  @Test
  void createCourseAssignment_withUnknownCourse_returns404() {
    CourseAssignmentSave request =
        new CourseAssignmentSave(
            "unknown-course", "unknown-teacher", "unknown-group", "unknown-semester");

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(adminToken);
    HttpEntity<CourseAssignmentSave> entity = new HttpEntity<>(request, headers);

    ResponseEntity<String> response =
        restTemplate.postForEntity("/course-assignments", entity, String.class);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }
}
