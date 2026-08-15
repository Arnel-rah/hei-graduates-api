package com.example.demo.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.example.demo.entity.JGroup;
import com.example.demo.entity.JPromotion;
import com.example.demo.model.GroupRecord;
import org.junit.jupiter.api.Test;

class GroupMapperTest {

  @Test
  void toRecord_mapsAllFieldsCorrectly() {
    JPromotion promotion = JPromotion.builder().id("promo-4").build();

    JGroup group = JGroup.builder().id("group-1").ref("TN").promotion(promotion).build();

    GroupRecord record = GroupMapper.toRecord(group);

    assertEquals("group-1", record.id());
    assertEquals("TN", record.ref());
    assertEquals("promo-4", record.promotionId());
  }

  @Test
  void toRecord_handlesNullPromotion() {
    JGroup group = JGroup.builder().id("group-2").ref("EL").promotion(null).build();

    GroupRecord record = GroupMapper.toRecord(group);

    assertNull(record.promotionId());
  }
}
