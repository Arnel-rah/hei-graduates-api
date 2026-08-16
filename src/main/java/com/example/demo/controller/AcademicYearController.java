package com.example.demo.controller;

import com.example.demo.model.AcademicYearRecord;
import com.example.demo.model.AcademicYearSave;
import com.example.demo.service.AcademicYearService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AcademicYearController {

  private final AcademicYearService academicYearService;

  @PostMapping("/academic-years")
  public ResponseEntity<AcademicYearRecord> createAcademicYear(
      @RequestBody AcademicYearSave request) {
    AcademicYearRecord academicYear = academicYearService.create(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(academicYear);
  }

  @GetMapping("/promotions/{promotionId}/academic-years")
  public ResponseEntity<List<AcademicYearRecord>> getPromotionAcademicYears(
      @PathVariable String promotionId) {
    return ResponseEntity.ok(academicYearService.listByPromotion(promotionId));
  }

  @PostMapping("/academic-years/{academicYearId}/publish")
  public ResponseEntity<AcademicYearRecord> publishAcademicYear(
      @PathVariable String academicYearId) {
    return ResponseEntity.ok(academicYearService.publish(academicYearId));
  }
}
