package com.example.demo.mapper;

import com.example.demo.entity.JGroup;
import com.example.demo.model.GroupRecord;

public class GroupMapper {

  public static GroupRecord toRecord(JGroup group) {
    return new GroupRecord(
        group.getId(),
        group.getRef(),
        group.getPromotion() != null ? group.getPromotion().getId() : null);
  }
}
