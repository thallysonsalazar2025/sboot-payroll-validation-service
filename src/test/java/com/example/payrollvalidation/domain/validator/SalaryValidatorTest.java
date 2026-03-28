package com.example.payrollvalidation.domain.validator;

import com.example.payrollvalidation.dto.PayrollValidationRequest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SalaryValidatorTest {

    private final SalaryValidator validator = new SalaryValidator();

    @Test
    void shouldReturnErrorWhenSalaryIsInvalid() {
        PayrollValidationRequest request = new PayrollValidationRequest("EMP001", BigDecimal.ZERO, BigDecimal.ONE, BigDecimal.ZERO, List.of(), "2026-03");

        List<String> errors = validator.validate(request);

        assertEquals(1, errors.size());
    }

    @Test
    void shouldNotReturnErrorWhenSalaryIsValid() {
        PayrollValidationRequest request = new PayrollValidationRequest("EMP001", BigDecimal.valueOf(5000), BigDecimal.ONE, BigDecimal.ZERO, List.of(), "2026-03");

        List<String> errors = validator.validate(request);

        assertTrue(errors.isEmpty());
    }
}
