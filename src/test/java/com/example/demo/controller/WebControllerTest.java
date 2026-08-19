package com.example.demo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.example.demo.model.PromotionRecord;
import com.example.demo.model.PromotionStudentView;
import com.example.demo.service.AcademicResultService;
import com.example.demo.service.GraduateExportService;
import com.example.demo.service.PromotionService;
import java.math.BigDecimal;
import java.net.URI;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

@ExtendWith(MockitoExtension.class)
class WebControllerTest {

  @Mock private PromotionService promotionService;

  @Mock private AcademicResultService academicResultService;

  @Mock private GraduateExportService graduateExportService;

  @InjectMocks private WebController webController;

  @Test
  void listPromotions_populatesModelAndReturnsPromotionsView() {
    List<PromotionRecord> promotions =
        List.of(new PromotionRecord("promo-1", "Promotion Demo", Instant.now()));
    when(promotionService.listAll()).thenReturn(promotions);

    Model model = new ExtendedModelMap();
    String view = webController.listPromotions(model);

    assertEquals("promotions", view);
    assertEquals(promotions, model.getAttribute("promotions"));
  }

  @Test
  void listStudents_populatesModelAndReturnsStudentsView() {
    List<PromotionStudentView> students =
        List.of(
            new PromotionStudentView(
                "student-1", "STU001", "Rahaingo", "Nel", new BigDecimal("14.20"), true, "TN"));
    when(academicResultService.listPromotionStudentsWithStatus("promo-1")).thenReturn(students);

    Model model = new ExtendedModelMap();
    String view = webController.listStudents("promo-1", model);

    assertEquals("students", view);
    assertEquals("promo-1", model.getAttribute("promotionId"));
    assertEquals(students, model.getAttribute("students"));
  }

  @Test
  void exportGraduates_returnsRedirectToPresignedUri() throws Exception {
    URI presignedUri = new URI("https://bucket.s3.amazonaws.com/graduates/promo-1-123.xlsx");
    when(graduateExportService.exportGraduatesToExcel("promo-1", null)).thenReturn(presignedUri);

    String result = webController.exportGraduates("promo-1", null);

    assertTrue(result.startsWith("redirect:"));
    assertEquals("redirect:" + presignedUri, result);
  }

  @Test
  void exportGraduates_withTrackFilter_passesFilterThrough() throws Exception {
    URI presignedUri = new URI("https://bucket.s3.amazonaws.com/graduates/promo-1-tn.xlsx");
    when(graduateExportService.exportGraduatesToExcel("promo-1", "TN")).thenReturn(presignedUri);

    String result = webController.exportGraduates("promo-1", "TN");

    assertEquals("redirect:" + presignedUri, result);
  }
}
