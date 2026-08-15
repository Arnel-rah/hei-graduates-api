package com.example.demo.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.example.demo.entity.JAccount;
import com.example.demo.entity.JTeacher;
import com.example.demo.model.TeacherRecord;
import org.junit.jupiter.api.Test;

class TeacherMapperTest {

  @Test
  void toRecord_mapsAllFieldsCorrectly() {
    JAccount account = JAccount.builder().id("acc-1").build();

    JTeacher teacher = JTeacher.builder().id("teacher-1").name("Rakoto").account(account).build();

    TeacherRecord record = TeacherMapper.toRecord(teacher);

    assertEquals("teacher-1", record.id());
    assertEquals("Rakoto", record.name());
    assertEquals("acc-1", record.accountId());
  }

  @Test
  void toRecord_handlesNullAccount() {
    JTeacher teacher = JTeacher.builder().id("teacher-2").name("Rasoa").account(null).build();

    TeacherRecord record = TeacherMapper.toRecord(teacher);

    assertNull(record.accountId());
  }
}
