package com.example.demo.service.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.demo.endpoint.event.model.TranscriptEmailRequested;
import com.example.demo.entity.JAccount;
import com.example.demo.entity.JStudent;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.file.bucket.BucketComponent;
import com.example.demo.mail.Email;
import com.example.demo.mail.Mailer;
import com.example.demo.model.Role;
import com.example.demo.model.TranscriptRecord;
import com.example.demo.model.TranscriptStatus;
import com.example.demo.repository.StudentRepository;
import com.example.demo.service.AcademicResultService;
import java.io.File;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TranscriptEmailRequestedServiceTest {

  @Mock private StudentRepository studentRepository;

  @Mock private AcademicResultService academicResultService;

  @Mock private BucketComponent bucketComponent;

  @Mock private Mailer mailer;

  @InjectMocks private TranscriptEmailRequestedService service;

  private JStudent buildStudent() {
    JAccount account =
        JAccount.builder()
            .id("acc-1")
            .email("nylalaina@hei.mg")
            .password("hashed")
            .role(Role.STUDENT)
            .build();

    return JStudent.builder()
        .id("student-1")
        .name("Ralaivao")
        .firstName("NyLalaina")
        .ref("STU001")
        .account(account)
        .build();
  }

  @Test
  void accept_generatesPdf_uploadsIt_andSendsEmail() throws Exception {
    JStudent student = buildStudent();
    when(studentRepository.findById("student-1")).thenReturn(Optional.of(student));

    TranscriptRecord transcript =
        new TranscriptRecord(
            "student-1", TranscriptStatus.COMPLET, new BigDecimal("15.00"), 30, 30, List.of());
    when(academicResultService.computeTranscript("student-1")).thenReturn(transcript);

    URL fakeUrl =
        URI.create("https://bucket.s3.amazonaws.com/transcripts/student-1-123.pdf").toURL();
    when(bucketComponent.presign(anyString(), any())).thenReturn(fakeUrl);

    TranscriptEmailRequested event =
        TranscriptEmailRequested.builder().studentId("student-1").build();

    service.accept(event);

    verify(bucketComponent).upload(any(File.class), anyString());

    ArgumentCaptor<Email> emailCaptor = ArgumentCaptor.forClass(Email.class);
    verify(mailer).accept(emailCaptor.capture());

    Email sentEmail = emailCaptor.getValue();
    assertEquals("nylalaina@hei.mg", sentEmail.to().getAddress());
  }

  @Test
  void accept_withUnknownStudent_throwsException() {
    when(studentRepository.findById("unknown")).thenReturn(Optional.empty());

    TranscriptEmailRequested event =
        TranscriptEmailRequested.builder().studentId("unknown").build();

    assertThrows(ResourceNotFoundException.class, () -> service.accept(event));
  }
}
