package com.example.demo.model;

import java.math.BigDecimal;

public record SemesterResultRecord(
    String semesterId,
    String semesterLabel,
    BigDecimal average,
    int creditsEarned,
    int creditsExpected) {}
