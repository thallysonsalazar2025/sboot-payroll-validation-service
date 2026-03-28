package com.example.payrollvalidation.application;

import com.example.payrollvalidation.domain.validator.EmployeeValidator;
import com.example.payrollvalidation.domain.validator.HoursValidator;
import com.example.payrollvalidation.domain.validator.PayrollValidator;
import com.example.payrollvalidation.domain.validator.SalaryValidator;
import com.example.payrollvalidation.dto.PayrollValidationRequest;
import com.example.payrollvalidation.dto.PayrollValidationResponse;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PayrollValidationServiceImplTest {

    @Test
    void shouldAggregateMultipleErrors() {
        List<PayrollValidator> validators = List.of(new EmployeeValidator(), new SalaryValidator(), new HoursValidator());
        PayrollValidationServiceImpl service = new PayrollValidationServiceImpl(validators);

        PayrollValidationRequest request = new PayrollValidationRequest("", BigDecimal.ZERO, BigDecimal.valueOf(-10), BigDecimal.valueOf(200), List.of(), "2026-03");

        PayrollValidationResponse response = service.validate(request);

        assertFalse(response.valid());
        assertTrue(response.errors().size() >= 3);
    }

    @Test
    void shouldReturnValidWhenNoErrors() {
        List<PayrollValidator> validators = List.of(new EmployeeValidator(), new SalaryValidator(), new HoursValidator());
        PayrollValidationServiceImpl service = new PayrollValidationServiceImpl(validators);

        PayrollValidationRequest request = new PayrollValidationRequest("EMP001", BigDecimal.valueOf(8000), BigDecimal.valueOf(160), BigDecimal.valueOf(10), List.of(), "2026-03");

        PayrollValidationResponse response = service.validate(request);

        assertTrue(response.valid());
        assertTrue(response.errors().isEmpty());
    }
}
