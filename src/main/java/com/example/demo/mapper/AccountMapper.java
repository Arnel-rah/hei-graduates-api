package com.example.demo.mapper;

import com.example.demo.entity.JAccount;
import com.example.demo.model.AccountRecord;
import com.example.demo.model.Role;

public class AccountMapper {

  public static AccountRecord toRecord(JAccount account) {
    return new AccountRecord(
        account.getId(), account.getEmail(), Role.valueOf(account.getRole().name()));
  }
}
