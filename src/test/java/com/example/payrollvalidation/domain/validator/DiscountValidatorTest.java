package com.example.payrollvalidation.domain.validator;

import com.example.payrollvalidation.dto.PayrollValidationRequest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DiscountValidatorTest {

    private final DiscountValidator validator = new DiscountValidator();

    @Test
    void shouldReturnErrorWhenAnyDiscountIsNegative() {
        PayrollValidationRequest request = new PayrollValidationRequest("EMP001", BigDecimal.TEN, BigDecimal.ONE, BigDecimal.ZERO, List.of(BigDecimal.ONE, BigDecimal.valueOf(-5)), "2026-03");

        List<String> errors = validator.validate(request);

        assertEquals(1, errors.size());
    }

    @Test
    void shouldNotReturnErrorWhenDiscountsAreValid() {
        PayrollValidationRequest request = new PayrollValidationRequest("EMP001", BigDecimal.TEN, BigDecimal.ONE, BigDecimal.ZERO, List.of(BigDecimal.ONE, BigDecimal.valueOf(5)), "2026-03");

        List<String> errors = validator.validate(request);

        assertTrue(errors.isEmpty());
    }
}
