package com.example.payrollvalidation.domain.validator;

import com.example.payrollvalidation.dto.PayrollValidationRequest;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
@Order(4)
public class DiscountValidator implements PayrollValidator {

    @Override
    public List<String> validate(PayrollValidationRequest request) {
        List<String> errors = new ArrayList<>();

        if (request.discounts() != null && request.discounts().stream().anyMatch(discount -> discount == null || discount.compareTo(BigDecimal.ZERO) < 0)) {
            errors.add("discounts não podem conter valores negativos ou nulos");
        }

        return errors;
    }
}
