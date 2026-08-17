package com.example.demo.controller;

import com.example.demo.service.GraduateExportService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GraduateExportController {

  private final GraduateExportService graduateExportService;

  @GetMapping("/promotions/{promotionId}/graduates/export")
  public ResponseEntity<Void> exportPromotionGraduates(
      @PathVariable String promotionId, @RequestParam(required = false) String track) {
    URI presignedUri = graduateExportService.exportGraduatesToExcel(promotionId, track);

    return ResponseEntity.status(HttpStatus.FOUND).location(presignedUri).build();
  }
}
