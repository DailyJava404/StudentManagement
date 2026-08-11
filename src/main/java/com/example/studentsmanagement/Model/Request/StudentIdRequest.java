package com.example.studentsmanagement.Model.Request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record StudentIdRequest(@NotNull(message = "studentId is required") @Positive(message = "studentId must be a positive number") Long studentId) {

}
