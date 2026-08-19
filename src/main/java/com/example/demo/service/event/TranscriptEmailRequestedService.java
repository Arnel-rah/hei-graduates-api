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
import java.awt.Color;
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
        var boldFont = PDType1Font.HELVETICA_BOLD;
        var regularFont = PDType1Font.HELVETICA;

        Color primaryColor = new Color(30, 58, 138);
        Color secondaryColor = new Color(100, 116, 139);
        Color cardBgColor = new Color(248, 250, 252);
        Color borderColor = new Color(226, 232, 240);
        Color textColor = new Color(15, 23, 42);

        stream.setNonStrokingColor(primaryColor);
        stream.addRect(0, 770, 595, 72);
        stream.fill();

        stream.beginText();
        stream.setFont(boldFont, 18);
        stream.setNonStrokingColor(Color.WHITE);
        stream.newLineAtOffset(50, 805);
        stream.showText("RELEVÉ DE NOTES ACADÉMIQUE");
        stream.endText();

        stream.beginText();
        stream.setFont(regularFont, 10);
        stream.setNonStrokingColor(new Color(226, 232, 240));
        stream.newLineAtOffset(50, 787);
        stream.showText("HEI - Haute École d'Informatique");
        stream.endText();

        float infoTopY = 740;

        drawCard(stream, 50, infoTopY - 95, cardBgColor, borderColor);

        stream.beginText();
        stream.setFont(boldFont, 10);
        stream.setNonStrokingColor(primaryColor);
        stream.newLineAtOffset(65, infoTopY - 22);
        stream.showText("INFORMATIONS ÉTUDIANT");
        stream.endText();

        stream.beginText();
        stream.setFont(boldFont, 11);
        stream.setNonStrokingColor(textColor);
        stream.newLineAtOffset(65, infoTopY - 45);
        stream.showText(student.getFirstName() + " " + student.getName());
        stream.endText();

        stream.beginText();
        stream.setFont(regularFont, 10);
        stream.setNonStrokingColor(secondaryColor);
        stream.newLineAtOffset(65, infoTopY - 67);
        stream.showText("Référence : " + student.getRef());
        stream.endText();

        drawCard(stream, 305, infoTopY - 95, cardBgColor, borderColor);

        stream.beginText();
        stream.setFont(boldFont, 10);
        stream.setNonStrokingColor(primaryColor);
        stream.newLineAtOffset(320, infoTopY - 22);
        stream.showText("BILAN ACADÉMIQUE");
        stream.endText();

        drawLabelValue(
            stream,
            boldFont,
            regularFont,
            textColor,
            secondaryColor,
            infoTopY - 43,
            "Statut : ",
            String.valueOf(transcript.status()));
        drawLabelValue(
            stream,
            boldFont,
            regularFont,
            textColor,
            secondaryColor,
            infoTopY - 60,
            "Moyenne générale : ",
            transcript.generalAverage() + " / 20");
        drawLabelValue(
            stream,
            boldFont,
            regularFont,
            textColor,
            secondaryColor,
            infoTopY - 77,
            "Crédits obtenus : ",
            transcript.totalCreditsEarned() + " / " + transcript.totalCreditsExpected());

        float tableTopY = infoTopY - 125;

        stream.setNonStrokingColor(primaryColor);
        stream.addRect(50, tableTopY - 25, 495, 25);
        stream.fill();

        stream.beginText();
        stream.setFont(boldFont, 10);
        stream.setNonStrokingColor(Color.WHITE);
        stream.newLineAtOffset(65, tableTopY - 17);
        stream.showText("SEMESTRE");
        stream.newLineAtOffset(220, 0);
        stream.showText("MOYENNE");
        stream.newLineAtOffset(110, 0);
        stream.showText("CRÉDITS OBTENUS");
        stream.endText();

        float currentY = tableTopY - 25;
        boolean alternate = false;

        for (SemesterResultRecord semester : transcript.semesters()) {
          float rowHeight = 24;
          currentY -= rowHeight;

          if (alternate) {
            stream.setNonStrokingColor(cardBgColor);
            stream.addRect(50, currentY, 495, rowHeight);
            stream.fill();
          }

          stream.setStrokingColor(borderColor);
          stream.setLineWidth(0.5f);
          stream.moveTo(50, currentY);
          stream.lineTo(545, currentY);
          stream.stroke();

          stream.beginText();
          stream.setFont(regularFont, 10);
          stream.setNonStrokingColor(textColor);
          stream.newLineAtOffset(65, currentY + 7);
          stream.showText(semester.semesterLabel());

          stream.newLineAtOffset(220, 0);
          stream.showText(semester.average() + " / 20");

          stream.newLineAtOffset(110, 0);
          stream.showText(semester.creditsEarned() + " / " + semester.creditsExpected());
          stream.endText();

          alternate = !alternate;
        }

        stream.setStrokingColor(borderColor);
        stream.setLineWidth(1f);
        stream.moveTo(50, 50);
        stream.lineTo(545, 50);
        stream.stroke();

        stream.beginText();
        stream.setFont(regularFont, 8);
        stream.setNonStrokingColor(secondaryColor);
        stream.newLineAtOffset(50, 38);
        stream.showText("Document généré automatiquement - HEI. Valide sans signature.");
        stream.endText();
      }

      document.save(file);
    }

    return file;
  }

  @SneakyThrows
  private void drawCard(
      PDPageContentStream stream, float x, float y, Color bgColor, Color borderColor) {
    stream.setNonStrokingColor(bgColor);
    stream.addRect(x, y, (float) 240, (float) 95);
    stream.fill();

    stream.setStrokingColor(borderColor);
    stream.setLineWidth(0.8f);
    stream.addRect(x, y, (float) 240, (float) 95);
    stream.stroke();
  }

  @SneakyThrows
  private void drawLabelValue(
      PDPageContentStream stream,
      PDType1Font boldFont,
      PDType1Font regularFont,
      Color textColor,
      Color secondaryColor,
      float y,
      String label,
      String value) {
    stream.beginText();
    stream.setFont(regularFont, 9);
    stream.setNonStrokingColor(secondaryColor);
    stream.newLineAtOffset((float) 320, y);
    stream.showText(label);

    stream.setFont(boldFont, 9);
    stream.setNonStrokingColor(textColor);
    stream.showText(value);
    stream.endText();
  }

  @SneakyThrows
  private void sendEmail(JStudent student, URL downloadUrl) {
    InternetAddress recipient = new InternetAddress(student.getAccount().getEmail());

    String body =
        """
<!DOCTYPE html>
<html>
<head>
  <style>
    body { font-family: Arial, sans-serif; color: #333333; line-height: 1.6; }
    .button {
      display: inline-block;
      padding: 12px 24px;
      font-size: 14px;
      color: #ffffff !important;
      background-color: #1e3a8a;
      text-decoration: none;
      border-radius: 6px;
      margin: 15px 0;
    }
  </style>
</head>
<body>
  <p>Bonjour %s,</p>
  <p>Votre relevé de notes est disponible. Vous pouvez le télécharger via le bouton ci-dessous (valide 7 jours) :</p>
  <p><a href="%s" class="button">Télécharger mon relevé de notes</a></p>
  <p>Cordialement,<br><strong>HEI</strong></p>
</body>
</html>
"""
            .formatted(student.getFirstName(), downloadUrl.toString());

    mailer.accept(
        new Email(recipient, List.of(), List.of(), "Votre relevé de notes HEI", body, List.of()));
  }
}
