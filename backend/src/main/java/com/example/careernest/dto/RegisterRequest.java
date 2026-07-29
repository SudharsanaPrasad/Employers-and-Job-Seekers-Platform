package com.example.careernest.dto;

import com.example.careernest.document.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(

        @NotBlank(message = "name is required")
        @Size(max = 100, message = "name must be at most 100 characters")
        String name,

        @NotBlank(message = "email is required")
        @Email(message = "email must be a valid email address")
        String email,

        @NotBlank(message = "password is required")
        @Size(min = 6, max = 100, message = "password must be at least 6 characters")
        String password,

        // international format is best for SMS, e.g. +9198XXXXXXXX
        @NotBlank(message = "phone is required")
        @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "phone must be 10 to 15 digits, optionally starting with +")
        String phone,

        @NotNull(message = "role is required (JOB_SEEKER or EMPLOYER)")
        Role role
) {
}
