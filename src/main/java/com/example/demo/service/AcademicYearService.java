package com.example.demo.service;

import com.example.demo.entity.JAcademicYear;
import com.example.demo.entity.JPromotion;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.AcademicYearMapper;
import com.example.demo.model.AcademicYearRecord;
import com.example.demo.model.AcademicYearSave;
import com.example.demo.repository.AcademicYearRepository;
import com.example.demo.repository.PromotionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AcademicYearService {

  private final AcademicYearRepository academicYearRepository;
  private final PromotionRepository promotionRepository;

  public AcademicYearRecord create(AcademicYearSave request) {
    JPromotion promotion =
        promotionRepository
            .findById(request.promotionId())
            .orElseThrow(() -> new ResourceNotFoundException("Promotion not found"));

    JAcademicYear academicYear =
        JAcademicYear.builder()
            .label(request.label())
            .order(request.order())
            .promotion(promotion)
            .published(false)
            .build();

    academicYearRepository.save(academicYear);

    return AcademicYearMapper.toRecord(academicYear);
  }

  public List<AcademicYearRecord> listByPromotion(String promotionId) {
    return academicYearRepository.findByPromotionId(promotionId).stream()
        .map(AcademicYearMapper::toRecord)
        .toList();
  }

  public AcademicYearRecord publish(String academicYearId) {
    JAcademicYear academicYear =
        academicYearRepository
            .findById(academicYearId)
            .orElseThrow(() -> new ResourceNotFoundException("Academic year not found"));

    academicYear.setPublished(true);
    academicYearRepository.save(academicYear);

    return AcademicYearMapper.toRecord(academicYear);
  }
}
