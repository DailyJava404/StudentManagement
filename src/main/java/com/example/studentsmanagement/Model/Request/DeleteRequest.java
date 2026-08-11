package com.example.studentsmanagement.Model.Request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record DeleteRequest(
        @NotNull(message = "Student ID is required")
        @Positive(message = "Student ID must be positive")
        Long studentId
) {
}