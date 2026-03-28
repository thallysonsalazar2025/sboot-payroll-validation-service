package com.example.payrollvalidation.domain.validator;

import com.example.payrollvalidation.dto.PayrollValidationRequest;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
@Order(3)
public class HoursValidator implements PayrollValidator {

    private static final BigDecimal MAX_OVERTIME_HOURS = BigDecimal.valueOf(100);

    @Override
    public List<String> validate(PayrollValidationRequest request) {
        List<String> errors = new ArrayList<>();

        if (request.workedHours() == null || request.workedHours().compareTo(BigDecimal.ZERO) < 0) {
            errors.add("workedHours deve ser maior ou igual a zero");
        }

        if (request.overtimeHours() == null || request.overtimeHours().compareTo(BigDecimal.ZERO) < 0) {
            errors.add("overtimeHours deve ser maior ou igual a zero");
        }

        if (request.overtimeHours() != null && request.overtimeHours().compareTo(MAX_OVERTIME_HOURS) > 0) {
            errors.add("overtimeHours não pode ser maior que 100");
        }

        return errors;
    }
}
