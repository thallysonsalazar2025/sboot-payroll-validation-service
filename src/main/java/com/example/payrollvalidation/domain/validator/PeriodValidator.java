package com.example.payrollvalidation.domain.validator;

import com.example.payrollvalidation.dto.PayrollValidationRequest;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Component
@Order(5)
public class PeriodValidator implements PayrollValidator {

    @Override
    public List<String> validate(PayrollValidationRequest request) {
        List<String> errors = new ArrayList<>();

        if (request.period() == null || request.period().isBlank()) {
            errors.add("period não pode ser nulo ou vazio");
            return errors;
        }

        try {
            YearMonth period = YearMonth.parse(request.period());
            YearMonth now = YearMonth.now();
            if (period.isAfter(now.plusMonths(1))) {
                errors.add("period não pode ser um período futuro inválido");
            }
        } catch (DateTimeParseException ex) {
            errors.add("period deve estar no formato yyyy-MM");
        }

        return errors;
    }
}
