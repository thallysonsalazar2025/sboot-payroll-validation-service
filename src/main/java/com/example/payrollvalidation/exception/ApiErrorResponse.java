package com.example.payrollvalidation.exception;

import java.time.Instant;
import java.util.List;

public record ApiErrorResponse(
        Instant timestamp,
        int status,
        String message,
        List<String> errors
) {
    public ApiErrorResponse {
        errors = List.copyOf(errors);
    }
}
