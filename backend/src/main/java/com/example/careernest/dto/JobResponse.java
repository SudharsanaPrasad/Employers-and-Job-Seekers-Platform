package com.example.careernest.dto;

import java.time.Instant;
import java.time.LocalDate;

public record JobResponse(
        String id,
        String title,
        String description,
        String location,
        String salary,
        LocalDate deadline,
        String employerId,
        String employerName,
        Instant createdAt
) {
}
