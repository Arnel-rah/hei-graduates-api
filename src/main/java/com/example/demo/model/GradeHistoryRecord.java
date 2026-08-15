package com.example.demo.model;

import java.math.BigDecimal;
import java.time.Instant;

public record GradeHistoryRecord(
    String id,
    BigDecimal oldValue,
    BigDecimal newValue,
    String reason,
    Instant modifiedAt,
    String gradeId,
    String modifiedByAccountId) {}
