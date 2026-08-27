package com.example.payrollvalidation.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public record PayrollValidationRequest(
        String employeeId,
        BigDecimal baseSalary,
        BigDecimal workedHours,
        BigDecimal overtimeHours,
        List<BigDecimal> discounts,
        String period
) {
    public PayrollValidationRequest {
        discounts = discounts == null ? null : Collections.unmodifiableList(new ArrayList<>(discounts));
    }
}
