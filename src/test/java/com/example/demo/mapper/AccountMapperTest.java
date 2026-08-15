package com.example.demo.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.demo.entity.JAccount;
import com.example.demo.model.AccountRecord;
import com.example.demo.model.Role;
import org.junit.jupiter.api.Test;

class AccountMapperTest {

  @Test
  void toRecord_mapsAllFieldsCorrectly() {
    JAccount account =
        JAccount.builder().id("acc-1").email("nel@hei.mg").role(Role.STUDENT).build();

    AccountRecord record = AccountMapper.toRecord(account);

    assertEquals("acc-1", record.id());
    assertEquals("nel@hei.mg", record.email());
    assertEquals(com.example.demo.model.Role.STUDENT, record.role());
  }
}
