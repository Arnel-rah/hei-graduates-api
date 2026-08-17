package com.example.demo.controller;

import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.entity.*;
import com.example.demo.model.GraduateRecord;
import com.example.demo.model.Role;
import com.example.demo.repository.*;
import java.math.BigDecimal;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

class GraduateControllerIT extends AbstractAdminIT {

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
  @Autowired private StudentGroupHistoryRepository studentGroupHistoryRepository;

  @Test
  void getGraduates_excludesStudentWhoFailedMandatoryCourse() {
    JPromotion promotion =
        JPromotion.builder()
            .label("Promotion " + System.nanoTime())
            .startYear(Instant.now())
            .build();
    promotionRepository.save(promotion);

    JGroup group = JGroup.builder().ref("TN").promotion(promotion).build();
    groupRepository.save(group);

    JSemester semester = JSemester.builder().label("S6").order(6).promotion(promotion).build();
    semesterRepository.save(semester);

    JCourse mandatoryCourse =
        JCourse.builder()
            .ref("MAND-" + System.nanoTime())
            .title("Mandatory Course")
            .credits(5)
            .mandatory(true)
            .build();
    courseRepository.save(mandatoryCourse);

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
            .course(mandatoryCourse)
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

    JStudent studentA = createStudentInPromotion(promotion, "Nel", "Rahaingo");
    gradeRepository.save(
        JGrade.builder().value(new BigDecimal("15.0")).student(studentA).exam(exam).build());
    linkStudentToGroup(studentA, group);

    JStudent studentB = createStudentInPromotion(promotion, "Yumei", "Chen");
    gradeRepository.save(
        JGrade.builder().value(new BigDecimal("8.0")).student(studentB).exam(exam).build());
    linkStudentToGroup(studentB, group);

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(adminToken);
    HttpEntity<Void> entity = new HttpEntity<>(headers);

    ResponseEntity<GraduateRecord[]> response =
        restTemplate.exchange(
            "/promotions/" + promotion.getId() + "/graduates",
            HttpMethod.GET,
            entity,
            GraduateRecord[].class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());

    boolean studentAPresent =
        java.util.Arrays.stream(response.getBody())
            .anyMatch(g -> g.studentId().equals(studentA.getId()));

    boolean studentBPresent =
        java.util.Arrays.stream(response.getBody())
            .anyMatch(g -> g.studentId().equals(studentB.getId()));

    assertTrue(studentAPresent, "A student who scores 15/20 in a mandatory course should graduate");

    assertFalse(
        studentBPresent, "A student who scores 8/20 in a mandatory course should NOT graduate");
  }

  private JStudent createStudentInPromotion(JPromotion promotion, String firstName, String name) {
    JAccount account =
        JAccount.builder()
            .email(name.toLowerCase() + "-" + System.nanoTime() + "@hei.mg")
            .password(passwordEncoder.encode("password123"))
            .role(Role.STUDENT)
            .build();
    accountRepository.save(account);

    JStudent student =
        JStudent.builder()
            .name(name)
            .firstName(firstName)
            .ref("STU-" + System.nanoTime())
            .account(account)
            .promotion(promotion)
            .build();
    studentRepository.save(student);

    return student;
  }

  private void linkStudentToGroup(JStudent student, JGroup group) {
    JStudentGroupHistory history =
        JStudentGroupHistory.builder()
            .startDate(Instant.now())
            .student(student)
            .group(group)
            .build();
    studentGroupHistoryRepository.save(history);
  }
}
