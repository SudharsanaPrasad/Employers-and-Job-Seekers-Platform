package com.example.careernest.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record JobRequest(

        @NotBlank(message = "title is required")
        @Size(max = 150, message = "title must be at most 150 characters")
        String title,

        @NotBlank(message = "description is required")
        @Size(max = 5000, message = "description must be at most 5000 characters")
        String description,

        @NotBlank(message = "location is required")
        @Size(max = 150, message = "location must be at most 150 characters")
        String location,

        @NotBlank(message = "salary is required")
        @Size(max = 100, message = "salary must be at most 100 characters")
        String salary,

        @NotNull(message = "deadline is required")
        @FutureOrPresent(message = "deadline cannot be in the past")
        LocalDate deadline
) {
}
