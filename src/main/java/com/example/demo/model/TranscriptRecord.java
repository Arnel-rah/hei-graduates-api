package com.example.demo.model;

import java.math.BigDecimal;
import java.util.List;

public record TranscriptRecord(
    String studentId,
    TranscriptStatus status,
    BigDecimal generalAverage,
    int totalCreditsEarned,
    int totalCreditsExpected,
    List<SemesterResultRecord> semesters) {}
