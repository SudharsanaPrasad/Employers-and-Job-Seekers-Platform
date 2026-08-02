package com.example.careernest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ApplicationRequest(

        @NotBlank(message = "jobId is required")
        String jobId,

        // optional note to the employer
        @Size(max = 2000, message = "coverLetter must be at most 2000 characters")
        String coverLetter
) {
}
