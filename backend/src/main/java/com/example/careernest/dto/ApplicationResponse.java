package com.example.careernest.dto;

import com.example.careernest.document.ApplicationStatus;

import java.time.Instant;

public record ApplicationResponse(
        String id,
        String jobId,
        String jobTitle,
        String seekerId,
        String seekerName,
        String employerId,
        ApplicationStatus status,
        String coverLetter,
        Instant appliedAt
) {
}
