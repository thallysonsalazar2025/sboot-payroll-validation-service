package com.example.payrollvalidation.domain.validator;

import com.example.payrollvalidation.dto.PayrollValidationRequest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HoursValidatorTest {

    private final HoursValidator validator = new HoursValidator();

    @Test
    void shouldReturnErrorsForNegativeAndAbsurdOvertime() {
        PayrollValidationRequest request = new PayrollValidationRequest("EMP001", BigDecimal.TEN, BigDecimal.valueOf(-1), BigDecimal.valueOf(101), List.of(), "2026-03");

        List<String> errors = validator.validate(request);

        assertEquals(2, errors.size());
    }

    @Test
    void shouldNotReturnErrorWhenHoursAreValid() {
        PayrollValidationRequest request = new PayrollValidationRequest("EMP001", BigDecimal.TEN, BigDecimal.valueOf(160), BigDecimal.valueOf(10), List.of(), "2026-03");

        List<String> errors = validator.validate(request);

        assertTrue(errors.isEmpty());
    }
}
