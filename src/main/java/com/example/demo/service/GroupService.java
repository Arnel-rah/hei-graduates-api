package com.example.demo.service;

import com.example.demo.entity.JGroup;
import com.example.demo.entity.JPromotion;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.GroupMapper;
import com.example.demo.model.GroupRecord;
import com.example.demo.model.GroupSave;
import com.example.demo.repository.GroupRepository;
import com.example.demo.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupService {

  private final GroupRepository groupRepository;
  private final PromotionRepository promotionRepository;

  public GroupRecord create(GroupSave request) {
    JPromotion promotion =
        promotionRepository
            .findById(request.promotionId())
            .orElseThrow(() -> new ResourceNotFoundException("Promotion not found"));

    JGroup group = JGroup.builder().ref(request.ref()).promotion(promotion).build();

    groupRepository.save(group);

    return GroupMapper.toRecord(group);
  }
}
