package com.example.demo.model;

import java.math.BigDecimal;

public record GradeRecord(String id, BigDecimal value, String studentId, String examId) {}
