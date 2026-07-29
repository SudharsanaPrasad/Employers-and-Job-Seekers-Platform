package com.example.careernest.dto;

import com.example.careernest.document.Role;

public record AuthResponse(
        String token,
        String userId,
        String name,
        String email,
        Role role
) {
}
