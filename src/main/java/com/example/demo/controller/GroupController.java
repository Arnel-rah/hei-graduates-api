package com.example.demo.controller;

import com.example.demo.model.GroupRecord;
import com.example.demo.model.GroupSave;
import com.example.demo.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/groups")
@RequiredArgsConstructor
public class GroupController {

  private final GroupService groupService;

  @PostMapping
  public ResponseEntity<GroupRecord> createGroup(@RequestBody GroupSave request) {
    GroupRecord group = groupService.create(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(group);
  }
}
