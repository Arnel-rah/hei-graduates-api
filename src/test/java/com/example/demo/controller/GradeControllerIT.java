package com.example.demo.controller;

import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.entity.*;
import com.example.demo.model.GradeRecord;
import com.example.demo.model.GradeSave;
import com.example.demo.model.Role;
import com.example.demo.repository.*;
import java.math.BigDecimal;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

class GradeControllerIT extends AbstractAdminIT {

  @Autowired private TestRestTemplate restTemplate;

  @Autowired private PromotionRepository promotionRepository;
  @Autowired private GroupRepository groupRepository;
  @Autowired private SemesterRepository semesterRepository;
  @Autowired private CourseRepository courseRepository;
  @Autowired private TeacherRepository teacherRepository;
  @Autowired private CourseAssignmentRepository courseAssignmentRepository;
  @Autowired private ExamRepository examRepository;
  @Autowired private StudentRepository studentRepository;
  @Autowired private GradeHistoryRepository gradeHistoryRepository;

  private String assignedTeacherToken;
  private String otherTeacherToken;
  private String examId;
  private String studentId;

  @BeforeEach
  void setUpFixture() {
    JPromotion promotion =
        JPromotion.builder()
            .label("Promotion " + System.nanoTime())
            .startYear(Instant.now())
            .build();
    promotionRepository.save(promotion);

    JGroup group = JGroup.builder().ref("TN").promotion(promotion).build();
    groupRepository.save(group);

    JSemester semester = JSemester.builder().label("S4").order(4).promotion(promotion).build();
    semesterRepository.save(semester);

    JCourse course =
        JCourse.builder()
            .ref("RES401")
            .title("Computer Networks")
            .credits(4)
            .mandatory(true)
            .build();
    courseRepository.save(course);

    JAccount assignedTeacherAccount =
        JAccount.builder()
            .email("assigned-" + System.nanoTime() + "@hei.mg")
            .password(passwordEncoder.encode("password123"))
            .role(Role.TEACHER)
            .build();
    accountRepository.save(assignedTeacherAccount);

    JTeacher assignedTeacher =
        JTeacher.builder().name("Rasoa").account(assignedTeacherAccount).build();
    teacherRepository.save(assignedTeacher);

    assignedTeacherToken = jwtService.generateToken(assignedTeacherAccount);

    JAccount otherTeacherAccount =
        JAccount.builder()
            .email("other-" + System.nanoTime() + "@hei.mg")
            .password(passwordEncoder.encode("password123"))
            .role(Role.TEACHER)
            .build();
    accountRepository.save(otherTeacherAccount);

    otherTeacherToken = jwtService.generateToken(otherTeacherAccount);

    JCourseAssignment assignment =
        JCourseAssignment.builder()
            .course(course)
            .teacher(assignedTeacher)
            .group(group)
            .semester(semester)
            .build();
    courseAssignmentRepository.save(assignment);

    JExam exam =
        JExam.builder()
            .dateExam(Instant.now())
            .coefficient(new BigDecimal("2.0"))
            .courseAssignment(assignment)
            .build();
    examRepository.save(exam);

    examId = exam.getId();

    JAccount studentAccount =
        JAccount.builder()
            .email("student-" + System.nanoTime() + "@hei.mg")
            .password(passwordEncoder.encode("password123"))
            .role(Role.STUDENT)
            .build();
    accountRepository.save(studentAccount);

    JStudent student =
        JStudent.builder()
            .name("Rahaingo")
            .firstName("Nel")
            .ref("STU001")
            .account(studentAccount)
            .promotion(promotion)
            .build();
    studentRepository.save(student);

    studentId = student.getId();
  }

  private ResponseEntity<GradeRecord> putGrade(String token, GradeSave request) {
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(token);

    HttpEntity<GradeSave> entity = new HttpEntity<>(request, headers);

    return restTemplate.exchange(
        "/exams/" + examId + "/grades/" + studentId, HttpMethod.PUT, entity, GradeRecord.class);
  }

  @Test
  void firstGrade_asAssignedTeacher_createsGradeWithoutHistory() {
    ResponseEntity<GradeRecord> response =
        putGrade(assignedTeacherToken, new GradeSave(new BigDecimal("15.5"), null));

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(0, new BigDecimal("15.5").compareTo(response.getBody().value()));

    assertEquals(
        0,
        gradeHistoryRepository.findByGradeIdOrderByModifiedAtAsc(response.getBody().id()).size());
  }

  @Test
  void updateGrade_withoutReason_returns400() {
    putGrade(assignedTeacherToken, new GradeSave(new BigDecimal("12.0"), null));

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(assignedTeacherToken);

    HttpEntity<GradeSave> entity =
        new HttpEntity<>(new GradeSave(new BigDecimal("14.0"), null), headers);

    ResponseEntity<String> response =
        restTemplate.exchange(
            "/exams/" + examId + "/grades/" + studentId, HttpMethod.PUT, entity, String.class);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  void updateGrade_withReason_createsHistoryEntry() {
    ResponseEntity<GradeRecord> first =
        putGrade(assignedTeacherToken, new GradeSave(new BigDecimal("10.0"), null));

    ResponseEntity<GradeRecord> updated =
        putGrade(
            assignedTeacherToken,
            new GradeSave(new BigDecimal("13.5"), "Student complaint - data entry error"));

    assertEquals(HttpStatus.OK, updated.getStatusCode());
    assertNotNull(updated.getBody());
    assertEquals(0, new BigDecimal("13.5").compareTo(updated.getBody().value()));

    assertNotNull(first.getBody());

    var history = gradeHistoryRepository.findByGradeIdOrderByModifiedAtAsc(first.getBody().id());

    assertEquals(1, history.size());
    assertEquals(0, new BigDecimal("10.0").compareTo(history.getFirst().getOldValue()));
    assertEquals(0, new BigDecimal("13.5").compareTo(history.getFirst().getNewValue()));
    assertEquals("Student complaint - data entry error", history.getFirst().getReason());
  }

  @Test
  void setGrade_asUnassignedTeacher_returns403() {
    ResponseEntity<GradeRecord> response =
        putGrade(otherTeacherToken, new GradeSave(new BigDecimal("11.0"), null));

    assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
  }

  @Test
  void setGrade_asAdmin_bypassesTeacherAssignmentCheck() {
    ResponseEntity<GradeRecord> response =
        putGrade(adminToken, new GradeSave(new BigDecimal("9.0"), null));

    assertEquals(HttpStatus.OK, response.getStatusCode());
  }
}
