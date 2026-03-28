package com.example.payrollvalidation.domain.validator;

import com.example.payrollvalidation.dto.PayrollValidationRequest;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
@Order(2)
public class SalaryValidator implements PayrollValidator {

    @Override
    public List<String> validate(PayrollValidationRequest request) {
        List<String> errors = new ArrayList<>();

        if (request.baseSalary() == null || request.baseSalary().compareTo(BigDecimal.ZERO) <= 0) {
            errors.add("baseSalary deve ser maior que zero");
        }

        return errors;
    }
}
