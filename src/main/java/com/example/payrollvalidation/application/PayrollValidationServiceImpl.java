package com.example.payrollvalidation.application;

import com.example.payrollvalidation.domain.model.ValidationResult;
import com.example.payrollvalidation.domain.validator.PayrollValidator;
import com.example.payrollvalidation.dto.PayrollValidationRequest;
import com.example.payrollvalidation.dto.PayrollValidationResponse;
import com.example.payrollvalidation.service.PayrollValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PayrollValidationServiceImpl implements PayrollValidationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PayrollValidationServiceImpl.class);

    private final List<PayrollValidator> validators;

    public PayrollValidationServiceImpl(List<PayrollValidator> validators) {
        this.validators = validators;
    }

    @Override
    public PayrollValidationResponse validate(PayrollValidationRequest request) {
        LOGGER.info("Iniciando validação da folha para employeeId={}", request.employeeId());

        List<String> errors = new ArrayList<>();
        validators.forEach(validator -> errors.addAll(validator.validate(request)));

        ValidationResult validationResult = new ValidationResult(errors.isEmpty(), errors);

        LOGGER.info("Validação finalizada. valid={} totalErrors={}", validationResult.valid(), validationResult.errors().size());
        return new PayrollValidationResponse(validationResult.valid(), validationResult.errors());
    }
}
