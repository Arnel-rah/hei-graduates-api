package com.example.demo.controller;

import com.example.demo.model.GraduateRecord;
import com.example.demo.service.AcademicResultService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GraduateController {

  private final AcademicResultService academicResultService;

  @GetMapping("/promotions/{promotionId}/graduates")
  public List<GraduateRecord> getPromotionGraduates(
      @PathVariable String promotionId, @RequestParam(required = false) String track) {
    return academicResultService.computeGraduates(promotionId, track);
  }
}
