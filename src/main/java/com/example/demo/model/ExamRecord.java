package com.example.demo.model;

import java.math.BigDecimal;
import java.time.Instant;

public record ExamRecord(
    String id, Instant dateExam, BigDecimal coefficient, String courseAssignmentId) {}
