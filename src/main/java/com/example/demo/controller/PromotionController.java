package com.example.demo.controller;

import com.example.demo.model.PromotionRecord;
import com.example.demo.service.PromotionService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/promotions")
@RequiredArgsConstructor
public class PromotionController {

  private final PromotionService promotionService;

  @GetMapping
  public List<PromotionRecord> getPromotions() {
    return promotionService.listAll();
  }
}
