package com.example.demo.controller;

import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.entity.*;
import com.example.demo.model.Role;
import com.example.demo.model.TranscriptRecord;
import com.example.demo.model.TranscriptStatus;
import com.example.demo.repository.*;
import java.math.BigDecimal;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

class TranscriptControllerIT extends AbstractAdminIT {

  @Autowired private TestRestTemplate restTemplate;

  @Autowired private PromotionRepository promotionRepository;
  @Autowired private GroupRepository groupRepository;
  @Autowired private SemesterRepository semesterRepository;
  @Autowired private CourseRepository courseRepository;
  @Autowired private TeacherRepository teacherRepository;
  @Autowired private CourseAssignmentRepository courseAssignmentRepository;
  @Autowired private ExamRepository examRepository;
  @Autowired private StudentRepository studentRepository;
  @Autowired private GradeRepository gradeRepository;

  private JStudent createStudentWithOneGradedCourse(BigDecimal gradeValue, int credits) {
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
            .ref("ALGO-" + System.nanoTime())
            .title("Algo")
            .credits(credits)
            .mandatory(true)
            .build();
    courseRepository.save(course);

    JAccount teacherAccount =
        JAccount.builder()
            .email("teacher-" + System.nanoTime() + "@hei.mg")
            .password(passwordEncoder.encode("password123"))
            .role(Role.TEACHER)
            .build();
    accountRepository.save(teacherAccount);
    JTeacher teacher = JTeacher.builder().name("Rakoto").account(teacherAccount).build();
    teacherRepository.save(teacher);

    JCourseAssignment assignment =
        JCourseAssignment.builder()
            .course(course)
            .teacher(teacher)
            .group(group)
            .semester(semester)
            .build();
    courseAssignmentRepository.save(assignment);

    JExam exam =
        JExam.builder()
            .dateExam(Instant.now())
            .coefficient(new BigDecimal("1.0"))
            .courseAssignment(assignment)
            .build();
    examRepository.save(exam);

    JAccount studentAccount =
        JAccount.builder()
            .email("student-" + System.nanoTime() + "@hei.mg")
            .password(passwordEncoder.encode("password123"))
            .role(Role.STUDENT)
            .build();
    accountRepository.save(studentAccount);
    JStudent student =
        JStudent.builder()
            .name("Chen")
            .firstName("Yumei")
            .ref("STU-" + System.nanoTime())
            .account(studentAccount)
            .promotion(promotion)
            .build();
    studentRepository.save(student);

    JGrade grade = JGrade.builder().value(gradeValue).student(student).exam(exam).build();
    gradeRepository.save(grade);

    return student;
  }

  @Test
  void getTranscript_withAllExamsGraded_isComplet() {
    JStudent student = createStudentWithOneGradedCourse(new BigDecimal("15.0"), 5);

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(adminToken);
    HttpEntity<Void> entity = new HttpEntity<>(headers);

    ResponseEntity<TranscriptRecord> response =
        restTemplate.exchange(
            "/students/" + student.getId() + "/transcript",
            HttpMethod.GET,
            entity,
            TranscriptRecord.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(TranscriptStatus.COMPLET, response.getBody().status());
    assertEquals(new BigDecimal("15.00"), response.getBody().generalAverage());
    assertEquals(5, response.getBody().totalCreditsEarned());
    assertEquals(5, response.getBody().totalCreditsExpected());
  }

  @Test
  void getTranscript_belowTenAverage_earnsZeroCredits() {
    JStudent student = createStudentWithOneGradedCourse(new BigDecimal("8.0"), 5);

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(adminToken);
    HttpEntity<Void> entity = new HttpEntity<>(headers);

    ResponseEntity<TranscriptRecord> response =
        restTemplate.exchange(
            "/students/" + student.getId() + "/transcript",
            HttpMethod.GET,
            entity,
            TranscriptRecord.class);

    assertEquals(0, response.getBody().totalCreditsEarned());
    assertEquals(5, response.getBody().totalCreditsExpected());
  }

  @Test
  void getTranscript_asOtherStudent_returns403() {
    JStudent student = createStudentWithOneGradedCourse(new BigDecimal("12.0"), 5);

    JAccount otherStudentAccount =
        JAccount.builder()
            .email("other-student-" + System.nanoTime() + "@hei.mg")
            .password(passwordEncoder.encode("password123"))
            .role(Role.STUDENT)
            .build();
    accountRepository.save(otherStudentAccount);
    String otherStudentToken = jwtService.generateToken(otherStudentAccount);

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(otherStudentToken);
    HttpEntity<Void> entity = new HttpEntity<>(headers);

    ResponseEntity<String> response =
        restTemplate.exchange(
            "/students/" + student.getId() + "/transcript", HttpMethod.GET, entity, String.class);

    assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
  }
}
