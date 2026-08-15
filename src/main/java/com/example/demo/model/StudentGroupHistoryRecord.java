package com.example.demo.model;

import java.time.Instant;

public record StudentGroupHistoryRecord(
    String id, Instant startDate, Instant endDate, String studentId, String groupId) {}
