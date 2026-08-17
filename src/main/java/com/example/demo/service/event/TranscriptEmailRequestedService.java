package com.example.demo.service.event;

import static java.io.File.createTempFile;

import com.example.demo.endpoint.event.model.TranscriptEmailRequested;
import com.example.demo.entity.JStudent;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.file.bucket.BucketComponent;
import com.example.demo.mail.Email;
import com.example.demo.mail.Mailer;
import com.example.demo.model.SemesterResultRecord;
import com.example.demo.model.TranscriptRecord;
import com.example.demo.repository.StudentRepository;
import com.example.demo.service.AcademicResultService;
import jakarta.mail.internet.InternetAddress;
import java.io.File;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TranscriptEmailRequestedService implements Consumer<TranscriptEmailRequested> {

  private final StudentRepository studentRepository;
  private final AcademicResultService academicResultService;
  private final BucketComponent bucketComponent;
  private final Mailer mailer;

  @SneakyThrows
  @Override
  public void accept(TranscriptEmailRequested event) {
    JStudent student =
        studentRepository
            .findById(event.getStudentId())
            .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

    TranscriptRecord transcript = academicResultService.computeTranscript(student.getId());

    File pdfFile = generatePdf(student, transcript);

    String bucketKey = "transcripts/" + student.getId() + "-" + System.currentTimeMillis() + ".pdf";
    bucketComponent.upload(pdfFile, bucketKey);

    URL downloadUrl = bucketComponent.presign(bucketKey, Duration.ofDays(7));

    sendEmail(student, downloadUrl);
  }

  @SneakyThrows
  private File generatePdf(JStudent student, TranscriptRecord transcript) {
    File file = createTempFile("transcript-" + student.getId(), ".pdf");

    try (PDDocument document = new PDDocument()) {
      PDPage page = new PDPage();
      document.addPage(page);

      try (PDPageContentStream stream = new PDPageContentStream(document, page)) {
        var font = PDType1Font.HELVETICA_BOLD;
        var regularFont = PDType1Font.HELVETICA;

        float y = 750;
        stream.beginText();
        stream.setFont(font, 16);
        stream.newLineAtOffset(50, y);
        stream.showText("Releve de notes - " + student.getFirstName() + " " + student.getName());
        stream.endText();

        y -= 40;
        stream.beginText();
        stream.setFont(regularFont, 11);
        stream.newLineAtOffset(50, y);
        stream.showText("Reference : " + student.getRef());
        stream.endText();

        y -= 20;
        stream.beginText();
        stream.setFont(regularFont, 11);
        stream.newLineAtOffset(50, y);
        stream.showText("Statut du releve : " + transcript.status());
        stream.endText();

        y -= 20;
        stream.beginText();
        stream.setFont(regularFont, 11);
        stream.newLineAtOffset(50, y);
        stream.showText("Moyenne generale : " + transcript.generalAverage() + "/20");
        stream.endText();

        y -= 20;
        stream.beginText();
        stream.setFont(regularFont, 11);
        stream.newLineAtOffset(50, y);
        stream.showText(
            "Credits obtenus : "
                + transcript.totalCreditsEarned()
                + " / "
                + transcript.totalCreditsExpected());
        stream.endText();

        y -= 40;
        for (SemesterResultRecord semester : transcript.semesters()) {
          stream.beginText();
          stream.setFont(regularFont, 11);
          stream.newLineAtOffset(50, y);
          stream.showText(
              semester.semesterLabel()
                  + " : moyenne "
                  + semester.average()
                  + "/20 - credits "
                  + semester.creditsEarned()
                  + "/"
                  + semester.creditsExpected());
          stream.endText();
          y -= 18;
        }
      }

      document.save(file);
    }

    return file;
  }

  @SneakyThrows
  private void sendEmail(JStudent student, URL downloadUrl) {
    InternetAddress recipient = new InternetAddress(student.getAccount().getEmail());

    String body =
        "Bonjour "
            + student.getFirstName()
            + ",\n\n"
            + "Votre releve de notes est disponible via le lien suivant (valide 7 jours) :\n"
            + downloadUrl
            + "\n\n"
            + "Cordialement,\nHEI";

    mailer.accept(
        new Email(recipient, List.of(), List.of(), "Votre releve de notes HEI", body, List.of()));
  }
}
