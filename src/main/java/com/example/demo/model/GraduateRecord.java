package com.example.demo.model;

import java.math.BigDecimal;

public record GraduateRecord(
    int rank,
    String studentId,
    String studentRef,
    String name,
    String firstName,
    BigDecimal average,
    int totalCredits,
    String diplomaType) {}
