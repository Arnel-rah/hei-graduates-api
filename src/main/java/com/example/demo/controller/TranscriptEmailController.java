package com.example.demo.controller;

import com.example.demo.endpoint.event.EventProducer;
import com.example.demo.endpoint.event.model.TranscriptEmailRequested;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.TranscriptEmailRequest;
import com.example.demo.model.TranscriptEmailStatus;
import com.example.demo.repository.StudentRepository;
import com.example.demo.service.StudentService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TranscriptEmailController {

  private final EventProducer<TranscriptEmailRequested> eventProducer;
  private final StudentRepository studentRepository;
  private final StudentService studentService;

  @PostMapping("/students/{studentId}/transcript-emails")
  public ResponseEntity<TranscriptEmailRequest> sendStudentTranscriptByEmail(
      @PathVariable String studentId) {
    var student =
        studentRepository
            .findById(studentId)
            .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

    studentService.checkStudentOwnership(student);

    var event = TranscriptEmailRequested.builder().studentId(studentId).build();
    eventProducer.accept(List.of(event));

    var response =
        new TranscriptEmailRequest(
            UUID.randomUUID().toString(), studentId, TranscriptEmailStatus.PENDING);

    return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
  }
}
