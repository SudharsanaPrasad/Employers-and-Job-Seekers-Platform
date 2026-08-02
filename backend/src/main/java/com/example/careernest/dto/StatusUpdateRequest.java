package com.example.careernest.dto;

import com.example.careernest.document.ApplicationStatus;
import jakarta.validation.constraints.NotNull;

public record StatusUpdateRequest(

        @NotNull(message = "status is required (REVIEWED, ACCEPTED or REJECTED)")
        ApplicationStatus status
) {
}
