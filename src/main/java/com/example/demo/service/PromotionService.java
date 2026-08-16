package com.example.demo.service;

import com.example.demo.mapper.PromotionMapper;
import com.example.demo.model.PromotionRecord;
import com.example.demo.repository.PromotionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PromotionService {

  private final PromotionRepository promotionRepository;

  public List<PromotionRecord> listAll() {
    return promotionRepository.findAll().stream().map(PromotionMapper::toRecord).toList();
  }
}
