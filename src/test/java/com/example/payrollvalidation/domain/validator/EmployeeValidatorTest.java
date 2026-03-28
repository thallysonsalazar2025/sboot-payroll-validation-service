package com.example.payrollvalidation.domain.validator;

import com.example.payrollvalidation.dto.PayrollValidationRequest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EmployeeValidatorTest {

    private final EmployeeValidator validator = new EmployeeValidator();

    @Test
    void shouldReturnErrorWhenEmployeeIdIsBlank() {
        PayrollValidationRequest request = new PayrollValidationRequest(" ", BigDecimal.TEN, BigDecimal.ONE, BigDecimal.ZERO, List.of(), "2026-03");

        List<String> errors = validator.validate(request);

        assertFalse(errors.isEmpty());
    }

    @Test
    void shouldNotReturnErrorWhenEmployeeIdIsValid() {
        PayrollValidationRequest request = new PayrollValidationRequest("EMP001", BigDecimal.TEN, BigDecimal.ONE, BigDecimal.ZERO, List.of(), "2026-03");

        List<String> errors = validator.validate(request);

        assertTrue(errors.isEmpty());
    }
}
