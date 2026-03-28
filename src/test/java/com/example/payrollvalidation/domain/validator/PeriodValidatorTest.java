package com.example.payrollvalidation.domain.validator;

import com.example.payrollvalidation.dto.PayrollValidationRequest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PeriodValidatorTest {

    private final PeriodValidator validator = new PeriodValidator();

    @Test
    void shouldReturnErrorWhenPeriodFormatIsInvalid() {
        PayrollValidationRequest request = new PayrollValidationRequest("EMP001", BigDecimal.TEN, BigDecimal.ONE, BigDecimal.ZERO, List.of(), "03-2026");

        List<String> errors = validator.validate(request);

        assertEquals(1, errors.size());
    }

    @Test
    void shouldReturnErrorWhenPeriodIsInvalidFuture() {
        String invalidFuture = YearMonth.now().plusMonths(2).toString();
        PayrollValidationRequest request = new PayrollValidationRequest("EMP001", BigDecimal.TEN, BigDecimal.ONE, BigDecimal.ZERO, List.of(), invalidFuture);

        List<String> errors = validator.validate(request);

        assertEquals(1, errors.size());
    }

    @Test
    void shouldNotReturnErrorWhenPeriodIsValid() {
        String valid = YearMonth.now().toString();
        PayrollValidationRequest request = new PayrollValidationRequest("EMP001", BigDecimal.TEN, BigDecimal.ONE, BigDecimal.ZERO, List.of(), valid);

        List<String> errors = validator.validate(request);

        assertTrue(errors.isEmpty());
    }
}
