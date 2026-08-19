package com.example.demo.service;

import com.example.demo.entity.JCourse;
import com.example.demo.entity.JCourseAssignment;
import com.example.demo.entity.JExam;
import com.example.demo.entity.JGrade;
import com.example.demo.entity.JSemester;
import com.example.demo.entity.JStudent;
import com.example.demo.entity.JStudentGroupHistory;
import com.example.demo.model.GraduateRecord;
import com.example.demo.model.SemesterResultRecord;
import com.example.demo.model.TranscriptRecord;
import com.example.demo.model.TranscriptStatus;
import com.example.demo.repository.ExamRepository;
import com.example.demo.repository.GradeRepository;
import com.example.demo.repository.StudentGroupHistoryRepository;
import com.example.demo.repository.StudentRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AcademicResultService {

  private final GradeRepository gradeRepository;
  private final ExamRepository examRepository;
  private final StudentRepository studentRepository;
  private final StudentGroupHistoryRepository studentGroupHistoryRepository;

  private record CourseResult(
      JSemester semester, JCourse course, BigDecimal average, boolean allExamsGraded) {
    boolean isValidated() {
      return average.compareTo(BigDecimal.TEN) >= 0;
    }
  }

  private List<CourseResult> computeCourseResults(String studentId) {
    List<JGrade> grades = gradeRepository.findByStudentId(studentId);

    Map<String, List<JGrade>> gradesByAssignmentId =
        grades.stream()
            .collect(Collectors.groupingBy(g -> g.getExam().getCourseAssignment().getId()));

    List<CourseResult> results = new ArrayList<>();

    for (var entry : gradesByAssignmentId.entrySet()) {
      String assignmentId = entry.getKey();
      List<JGrade> assignmentGrades = entry.getValue();

      JCourseAssignment assignment = assignmentGrades.get(0).getExam().getCourseAssignment();

      BigDecimal weightedSum = BigDecimal.ZERO;
      BigDecimal coefficientSum = BigDecimal.ZERO;

      for (JGrade grade : assignmentGrades) {
        BigDecimal coefficient = grade.getExam().getCoefficient();
        weightedSum = weightedSum.add(grade.getValue().multiply(coefficient));
        coefficientSum = coefficientSum.add(coefficient);
      }

      BigDecimal courseAverage =
          coefficientSum.compareTo(BigDecimal.ZERO) == 0
              ? BigDecimal.ZERO
              : weightedSum.divide(coefficientSum, 2, RoundingMode.HALF_UP);

      List<JExam> allExamsForAssignment = examRepository.findByCourseAssignmentId(assignmentId);
      boolean allExamsGraded = allExamsForAssignment.size() == assignmentGrades.size();

      results.add(
          new CourseResult(
              assignment.getSemester(), assignment.getCourse(), courseAverage, allExamsGraded));
    }

    return results;
  }

  public TranscriptRecord computeTranscript(String studentId) {
    List<CourseResult> courseResults = computeCourseResults(studentId);

    boolean anyIncomplete = courseResults.stream().anyMatch(r -> !r.allExamsGraded());

    Map<JSemester, List<CourseResult>> bySemester =
        courseResults.stream().collect(Collectors.groupingBy(CourseResult::semester));

    List<SemesterResultRecord> semesterResults = new ArrayList<>();

    int totalCreditsEarned = 0;
    int totalCreditsExpected = 0;
    BigDecimal totalWeightedSum = BigDecimal.ZERO;
    BigDecimal totalCreditWeight = BigDecimal.ZERO;

    List<JSemester> orderedSemesters =
        bySemester.keySet().stream().sorted(Comparator.comparingInt(JSemester::getOrder)).toList();

    for (JSemester semester : orderedSemesters) {
      List<CourseResult> results = bySemester.get(semester);

      int creditsExpected = results.stream().mapToInt(r -> r.course().getCredits()).sum();
      int creditsEarned =
          results.stream()
              .filter(CourseResult::isValidated)
              .mapToInt(r -> r.course().getCredits())
              .sum();

      BigDecimal semWeightedSum = BigDecimal.ZERO;
      BigDecimal semCreditWeight = BigDecimal.ZERO;

      for (CourseResult r : results) {
        BigDecimal credits = BigDecimal.valueOf(r.course().getCredits());
        semWeightedSum = semWeightedSum.add(r.average().multiply(credits));
        semCreditWeight = semCreditWeight.add(credits);
      }

      BigDecimal semesterAverage =
          semCreditWeight.compareTo(BigDecimal.ZERO) == 0
              ? BigDecimal.ZERO
              : semWeightedSum.divide(semCreditWeight, 2, RoundingMode.HALF_UP);

      semesterResults.add(
          new SemesterResultRecord(
              semester.getId(),
              semester.getLabel(),
              semesterAverage,
              creditsEarned,
              creditsExpected));

      totalCreditsEarned += creditsEarned;
      totalCreditsExpected += creditsExpected;
      totalWeightedSum = totalWeightedSum.add(semWeightedSum);
      totalCreditWeight = totalCreditWeight.add(semCreditWeight);
    }

    BigDecimal generalAverage =
        totalCreditWeight.compareTo(BigDecimal.ZERO) == 0
            ? BigDecimal.ZERO
            : totalWeightedSum.divide(totalCreditWeight, 2, RoundingMode.HALF_UP);

    TranscriptStatus status = anyIncomplete ? TranscriptStatus.INCOMPLET : TranscriptStatus.COMPLET;

    return new TranscriptRecord(
        studentId,
        status,
        generalAverage,
        totalCreditsEarned,
        totalCreditsExpected,
        semesterResults);
  }

  public List<GraduateRecord> computeGraduates(String promotionId, String trackFilter) {
    List<JStudent> students = studentRepository.findByPromotionId(promotionId);

    record Candidate(JStudent student, BigDecimal average, int credits, String diplomaType) {}

    List<Candidate> candidates = new ArrayList<>();

    for (JStudent student : students) {
      List<CourseResult> courseResults = computeCourseResults(student.getId());

      boolean anyIncomplete = courseResults.stream().anyMatch(r -> !r.allExamsGraded());
      if (anyIncomplete) {
        continue; // dossier pas complet, pas eligible
      }

      boolean failedMandatoryCourse =
          courseResults.stream().anyMatch(r -> r.course().isMandatory() && !r.isValidated());
      if (failedMandatoryCourse) {
        continue; // regle metier : 10/20 minimum sur les matieres obligatoires
      }

      TranscriptRecord transcript = computeTranscript(student.getId());

      String diplomaType = resolveDiplomaType(student.getId());
      if (trackFilter != null && !trackFilter.equalsIgnoreCase(diplomaType)) {
        continue;
      }

      candidates.add(
          new Candidate(
              student, transcript.generalAverage(), transcript.totalCreditsEarned(), diplomaType));
    }

    candidates.sort(Comparator.comparing(Candidate::average).reversed());

    List<GraduateRecord> graduates = new ArrayList<>();
    int rank = 1;
    for (Candidate c : candidates) {
      graduates.add(
          new GraduateRecord(
              rank++,
              c.student().getId(),
              c.student().getRef(),
              c.student().getName(),
              c.student().getFirstName(),
              c.average(),
              c.credits(),
              c.diplomaType()));
    }

    return graduates;
  }

  private String resolveDiplomaType(String studentId) {
    List<JStudentGroupHistory> history =
        studentGroupHistoryRepository.findByStudentIdOrderByStartDateDesc(studentId);

    if (history.isEmpty()) {
      return null;
    }

    return history.get(0).getGroup().getRef();
  }

  public List<com.example.demo.model.PromotionStudentView> listPromotionStudentsWithStatus(
      String promotionId) {
    List<JStudent> students = studentRepository.findByPromotionId(promotionId);

    List<com.example.demo.model.PromotionStudentView> views = new ArrayList<>();

    for (JStudent student : students) {
      List<CourseResult> courseResults = computeCourseResults(student.getId());
      TranscriptRecord transcript = computeTranscript(student.getId());

      boolean anyIncomplete = courseResults.stream().anyMatch(r -> !r.allExamsGraded());
      boolean failedMandatory =
          courseResults.stream().anyMatch(r -> r.course().isMandatory() && !r.isValidated());
      boolean graduated = !anyIncomplete && !failedMandatory;

      String diplomaType = resolveDiplomaType(student.getId());

      views.add(
          new com.example.demo.model.PromotionStudentView(
              student.getId(),
              student.getRef(),
              student.getName(),
              student.getFirstName(),
              transcript.generalAverage(),
              graduated,
              diplomaType));
    }

    views.sort(
        Comparator.comparing(com.example.demo.model.PromotionStudentView::average).reversed());

    return views;
  }
}
