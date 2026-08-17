package com.example.demo.model;

import java.math.BigDecimal;
import java.time.Instant;

public record ExamSave(Instant dateExam, BigDecimal coefficient) {}
