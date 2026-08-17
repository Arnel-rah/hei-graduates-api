package com.example.demo.service;

import static java.io.File.createTempFile;

import com.example.demo.file.bucket.BucketComponent;
import com.example.demo.model.GraduateRecord;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;
import java.time.Duration;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GraduateExportService {

  private static final String[] HEADERS = {
    "Rang", "Reference", "Nom", "Prenom", "Moyenne", "Credits", "Diplome"
  };

  private final AcademicResultService academicResultService;
  private final BucketComponent bucketComponent;

  @SneakyThrows
  public URI exportGraduatesToExcel(String promotionId, String trackFilter) {
    List<GraduateRecord> graduates =
        academicResultService.computeGraduates(promotionId, trackFilter);

    File file = createTempFile("graduates-" + promotionId, ".xlsx");
    writeExcelFile(graduates, file);

    String bucketKey = "graduates/" + promotionId + "-" + System.currentTimeMillis() + ".xlsx";
    bucketComponent.upload(file, bucketKey);

    return bucketComponent.presign(bucketKey, Duration.ofMinutes(10)).toURI();
  }

  private void writeExcelFile(List<GraduateRecord> graduates, File file) throws Exception {
    try (Workbook workbook = new XSSFWorkbook()) {
      Sheet sheet = workbook.createSheet("Diplomes");

      Row header = sheet.createRow(0);
      for (int i = 0; i < HEADERS.length; i++) {
        header.createCell(i).setCellValue(HEADERS[i]);
      }

      int rowIndex = 1;
      for (GraduateRecord graduate : graduates) {
        Row row = sheet.createRow(rowIndex++);
        row.createCell(0).setCellValue(graduate.rank());
        row.createCell(1).setCellValue(graduate.studentRef());
        row.createCell(2).setCellValue(graduate.name());
        row.createCell(3).setCellValue(graduate.firstName());
        row.createCell(4).setCellValue(graduate.average().doubleValue());
        row.createCell(5).setCellValue(graduate.totalCredits());
        row.createCell(6)
            .setCellValue(graduate.diplomaType() != null ? graduate.diplomaType() : "");
      }

      for (int i = 0; i < HEADERS.length; i++) {
        sheet.autoSizeColumn(i);
      }

      try (FileOutputStream fos = new FileOutputStream(file)) {
        workbook.write(fos);
      }
    }
  }
}
