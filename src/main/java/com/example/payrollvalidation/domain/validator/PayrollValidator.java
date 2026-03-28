package com.example.payrollvalidation.domain.validator;

import com.example.payrollvalidation.dto.PayrollValidationRequest;

import java.util.List;

public interface PayrollValidator {

    List<String> validate(PayrollValidationRequest request);
}
