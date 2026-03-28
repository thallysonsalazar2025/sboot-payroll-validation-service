package com.example.payrollvalidation.dto;

import java.util.List;

public record PayrollValidationResponse(
        boolean valid,
        List<String> errors
) {
}
