package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.demo.file.bucket.BucketComponent;
import com.example.demo.model.GraduateRecord;
import java.io.File;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URL;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GraduateExportServiceTest {

  @Mock private AcademicResultService academicResultService;

  @Mock private BucketComponent bucketComponent;

  @InjectMocks private GraduateExportService graduateExportService;

  @Test
  void exportGraduatesToExcel_uploadsFileAndReturnsPresignedUri() throws Exception {
    List<GraduateRecord> graduates =
        List.of(
            new GraduateRecord(
                1,
                "student-1",
                "STU001",
                "Ralaivao",
                "NyLalaina",
                new BigDecimal("15.50"),
                30,
                "TN"));
    when(academicResultService.computeGraduates("promo-1", null)).thenReturn(graduates);

    URL fakeUrl = URI.create("https://bucket.s3.amazonaws.com/graduates/promo-1-123.xlsx").toURL();
    when(bucketComponent.presign(anyString(), any())).thenReturn(fakeUrl);

    URI result = graduateExportService.exportGraduatesToExcel("promo-1", null);

    assertEquals(fakeUrl.toURI(), result);
    verify(bucketComponent).upload(any(File.class), anyString());
  }

  @Test
  void exportGraduatesToExcel_withEmptyList_stillUploadsFile() throws Exception {
    when(academicResultService.computeGraduates("promo-empty", null)).thenReturn(List.of());

    URL fakeUrl =
        URI.create("https://bucket.s3.amazonaws.com/graduates/promo-empty-456.xlsx").toURL();
    when(bucketComponent.presign(anyString(), any())).thenReturn(fakeUrl);

    URI result = graduateExportService.exportGraduatesToExcel("promo-empty", null);

    assertEquals(fakeUrl.toURI(), result);
    verify(bucketComponent).upload(any(File.class), anyString());
  }
}
