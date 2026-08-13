package com.example.payrollvalidation.domain.model;

import java.util.List;

public record ValidationResult(
        boolean valid,
        List<String> errors
) {
    public ValidationResult {
        errors = List.copyOf(errors);
    }
}
