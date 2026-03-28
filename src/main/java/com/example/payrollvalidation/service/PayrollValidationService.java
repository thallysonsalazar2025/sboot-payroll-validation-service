package com.example.payrollvalidation.service;

import com.example.payrollvalidation.dto.PayrollValidationRequest;
import com.example.payrollvalidation.dto.PayrollValidationResponse;

public interface PayrollValidationService {

    PayrollValidationResponse validate(PayrollValidationRequest request);
}
