package com.example.backend.dto;

/** Result of the "Test Connection" action. status is "Success" or "Error". */
public record TestConnectionResponse(String status, String message) {
}
