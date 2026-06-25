package com.example.backend.dto;

/** Request body for {@code POST /api/auth/register}. */
public record RegisterRequest(String email, String password, String userName) {
}
