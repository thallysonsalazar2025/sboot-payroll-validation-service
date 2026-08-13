package com.example.payrollvalidation.dto;

import com.example.payrollvalidation.domain.model.ValidationResult;
import com.example.payrollvalidation.exception.ApiErrorResponse;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PayrollRecordsImmutabilityTest {

    @Test
    void shouldCopyMutableInputLists() {
        List<BigDecimal> discounts = new ArrayList<>(List.of(BigDecimal.ONE));
        List<String> errors = new ArrayList<>(List.of("invalid"));

        PayrollValidationRequest request = new PayrollValidationRequest(
                "EMP001", BigDecimal.TEN, BigDecimal.ONE, BigDecimal.ZERO, discounts, "2026-03"
        );
        PayrollValidationResponse response = new PayrollValidationResponse(false, errors);
        ValidationResult result = new ValidationResult(false, errors);
        ApiErrorResponse apiError = new ApiErrorResponse(Instant.EPOCH, 400, "invalid", errors);

        discounts.clear();
        errors.clear();

        assertEquals(List.of(BigDecimal.ONE), request.discounts());
        assertEquals(List.of("invalid"), response.errors());
        assertEquals(List.of("invalid"), result.errors());
        assertEquals(List.of("invalid"), apiError.errors());
        assertThrows(UnsupportedOperationException.class, () -> request.discounts().clear());
        assertThrows(UnsupportedOperationException.class, () -> response.errors().clear());
    }

    @Test
    void shouldPreserveNullableDiscountsContract() {
        PayrollValidationRequest request = new PayrollValidationRequest(
                "EMP001", BigDecimal.TEN, BigDecimal.ONE, BigDecimal.ZERO, null, "2026-03"
        );

        assertNull(request.discounts());
    }
}
