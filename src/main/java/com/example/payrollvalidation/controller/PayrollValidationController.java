package com.example.payrollvalidation.controller;

import com.example.payrollvalidation.dto.PayrollValidationRequest;
import com.example.payrollvalidation.dto.PayrollValidationResponse;
import com.example.payrollvalidation.service.PayrollValidationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payroll")
@Tag(name = "Payroll Validation", description = "API para validação de entrada da folha de pagamento")
public class PayrollValidationController {

    private final PayrollValidationService payrollValidationService;

    public PayrollValidationController(PayrollValidationService payrollValidationService) {
        this.payrollValidationService = payrollValidationService;
    }

    @PostMapping("/validate")
    @Operation(summary = "Valida dados de entrada da folha de pagamento")
    public ResponseEntity<PayrollValidationResponse> validate(@RequestBody PayrollValidationRequest request) {
        return ResponseEntity.ok(payrollValidationService.validate(request));
    }
}
