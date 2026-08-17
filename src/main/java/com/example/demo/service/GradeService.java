package com.example.demo.service;

import com.example.demo.entity.JAccount;
import com.example.demo.entity.JExam;
import com.example.demo.entity.JGrade;
import com.example.demo.entity.JGradeHistory;
import com.example.demo.entity.JStudent;
import com.example.demo.exception.ForbiddenOperationException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.GradeMapper;
import com.example.demo.model.GradeRecord;
import com.example.demo.model.GradeSave;
import com.example.demo.model.Role;
import com.example.demo.repository.ExamRepository;
import com.example.demo.repository.GradeHistoryRepository;
import com.example.demo.repository.GradeRepository;
import com.example.demo.repository.StudentRepository;
import java.time.Instant;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class GradeService {

  private final GradeRepository gradeRepository;
  private final GradeHistoryRepository gradeHistoryRepository;
  private final ExamRepository examRepository;
  private final StudentRepository studentRepository;
  private final CurrentUserService currentUserService;

  @Transactional
  public GradeRecord setStudentExamGrade(String examId, String studentId, GradeSave request) {
    JExam exam =
        examRepository
            .findById(examId)
            .orElseThrow(() -> new ResourceNotFoundException("Exam not found"));

    JStudent student =
        studentRepository
            .findById(studentId)
            .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

    JAccount currentAccount = currentUserService.getCurrentAccount();
    checkTeacherIsAssignedToCourse(exam, currentAccount);

    Optional<JGrade> existingGrade = gradeRepository.findByStudentIdAndExamId(studentId, examId);

    if (existingGrade.isPresent()) {
      return updateExistingGrade(existingGrade.get(), request, currentAccount);
    }

    return createNewGrade(exam, student, request);
  }

  private void checkTeacherIsAssignedToCourse(JExam exam, JAccount currentAccount) {
    if (currentAccount.getRole() != Role.TEACHER) {
      return;
    }

    String assignedTeacherAccountId = exam.getCourseAssignment().getTeacher().getAccount().getId();

    if (!assignedTeacherAccountId.equals(currentAccount.getId())) {
      throw new ForbiddenOperationException("You are not the teacher assigned to this course");
    }
  }

  private GradeRecord createNewGrade(JExam exam, JStudent student, GradeSave request) {
    JGrade grade = JGrade.builder().value(request.value()).student(student).exam(exam).build();

    gradeRepository.save(grade);

    return GradeMapper.toRecord(grade);
  }

  private GradeRecord updateExistingGrade(
      JGrade grade, GradeSave request, JAccount currentAccount) {
    if (!StringUtils.hasText(request.reason())) {
      throw new IllegalArgumentException("A reason is required when updating an existing grade");
    }

    JGradeHistory history =
        JGradeHistory.builder()
            .oldValue(grade.getValue())
            .newValue(request.value())
            .reason(request.reason())
            .modifiedAt(Instant.now())
            .grade(grade)
            .modifiedBy(currentAccount)
            .build();

    gradeHistoryRepository.save(history);

    grade.setValue(request.value());
    gradeRepository.save(grade);

    return GradeMapper.toRecord(grade);
  }
}
