package com.example.payrollvalidation.domain.validator;

import com.example.payrollvalidation.dto.PayrollValidationRequest;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Order(1)
public class EmployeeValidator implements PayrollValidator {

    @Override
    public List<String> validate(PayrollValidationRequest request) {
        List<String> errors = new ArrayList<>();
        if (request.employeeId() == null || request.employeeId().isBlank()) {
            errors.add("employeeId não pode ser nulo ou vazio");
        }
        return errors;
    }
}
