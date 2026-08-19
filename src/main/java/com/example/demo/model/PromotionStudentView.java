package com.example.demo.model;

import java.math.BigDecimal;

public record PromotionStudentView(
    String studentId,
    String ref,
    String name,
    String firstName,
    BigDecimal average,
    boolean graduated,
    String diplomaType) {}
