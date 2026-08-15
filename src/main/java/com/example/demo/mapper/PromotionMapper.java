package com.example.demo.mapper;

import com.example.demo.entity.JPromotion;
import com.example.demo.model.PromotionRecord;

public class PromotionMapper {

  public static PromotionRecord toRecord(JPromotion promotion) {
    return new PromotionRecord(promotion.getId(), promotion.getLabel(), promotion.getStartYear());
  }
}
