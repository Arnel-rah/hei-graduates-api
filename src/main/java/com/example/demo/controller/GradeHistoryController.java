package com.example.demo.controller;

import com.example.demo.model.GradeHistoryRecord;
import com.example.demo.service.GradeHistoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/grades/{gradeId}/history")
@RequiredArgsConstructor
public class GradeHistoryController {

  private final GradeHistoryService gradeHistoryService;

  @GetMapping
  public List<GradeHistoryRecord> getGradeHistory(@PathVariable String gradeId) {
    return gradeHistoryService.getHistory(gradeId);
  }
}
