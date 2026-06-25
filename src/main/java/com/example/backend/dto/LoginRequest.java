package com.example.backend.dto;

/** Request body for {@code POST /api/auth/login}. */
public record LoginRequest(String email, String password) {
}
