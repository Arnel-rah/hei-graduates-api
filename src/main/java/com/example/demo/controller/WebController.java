package com.example.demo.controller;

import com.example.demo.service.AcademicResultService;
import com.example.demo.service.GraduateExportService;
import com.example.demo.service.PromotionService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/web")
@RequiredArgsConstructor
public class WebController {

  private final PromotionService promotionService;
  private final AcademicResultService academicResultService;
  private final GraduateExportService graduateExportService;

  @GetMapping("/promotions")
  public String listPromotions(Model model) {
    model.addAttribute("promotions", promotionService.listAll());
    return "promotions";
  }

  @GetMapping("/promotions/{promotionId}/students")
  public String listStudents(@PathVariable String promotionId, Model model) {
    model.addAttribute("promotionId", promotionId);
    model.addAttribute(
        "students", academicResultService.listPromotionStudentsWithStatus(promotionId));
    return "students";
  }

  @GetMapping("/promotions/{promotionId}/graduates/export")
  public String exportGraduates(
      @PathVariable String promotionId, @RequestParam(required = false) String track) {
    URI presignedUri = graduateExportService.exportGraduatesToExcel(promotionId, track);
    return "redirect:" + presignedUri;
  }
}
