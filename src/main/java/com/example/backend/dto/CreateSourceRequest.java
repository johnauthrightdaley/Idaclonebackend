package com.example.backend.dto;

/** Body for the "Create New" source wizard (POST /api/sources). */
public record CreateSourceRequest(String name, String description, String sourceType) {
}
