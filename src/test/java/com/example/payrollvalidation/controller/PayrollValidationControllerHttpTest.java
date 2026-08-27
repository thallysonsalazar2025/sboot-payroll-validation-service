package com.example.payrollvalidation.controller;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.example.payrollvalidation.dto.PayrollValidationRequest;
import com.example.payrollvalidation.dto.PayrollValidationResponse;
import com.example.payrollvalidation.exception.GlobalExceptionHandler;
import com.example.payrollvalidation.service.PayrollValidationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

class PayrollValidationControllerHttpTest {

    private PayrollValidationService service;
    private MockMvc mockMvc;
    private ch.qos.logback.classic.Logger exceptionLogger;
    private ListAppender<ILoggingEvent> logHandler;

    @BeforeEach
    void setUp() {
        service = mock(PayrollValidationService.class);
        mockMvc = standaloneSetup(new PayrollValidationController(service))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        exceptionLogger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(GlobalExceptionHandler.class);
        logHandler = new ListAppender<>();
        logHandler.start();
        exceptionLogger.addAppender(logHandler);
    }

    @AfterEach
    void tearDown() {
        exceptionLogger.detachAppender(logHandler);
        logHandler.stop();
    }

    @Test
    void shouldPreserveSuccessfulHttpContract() throws Exception {
        when(service.validate(any())).thenReturn(new PayrollValidationResponse(true, List.of()));

        mockMvc.perform(post("/api/payroll/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "employeeId": "EMP001",
                                  "baseSalary": 1000.00,
                                  "workedHours": 160,
                                  "overtimeHours": 2,
                                  "discounts": [10.00],
                                  "period": "2026-03"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value(true))
                .andExpect(jsonPath("$.errors").isEmpty());

        ArgumentCaptor<PayrollValidationRequest> requestCaptor = ArgumentCaptor.forClass(PayrollValidationRequest.class);
        verify(service).validate(requestCaptor.capture());
        assertEquals(new BigDecimal("160"), requestCaptor.getValue().workedHours());
    }

    @Test
    void shouldSanitizeMalformedJsonResponse() throws Exception {
        String sensitiveValue = "employeeId=CPF-12345678900";

        mockMvc.perform(post("/api/payroll/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Correlation-ID", "b21541a5-e09e-442c-a736-bf42819d6d2c")
                        .content("{\"employeeId\":\"" + sensitiveValue + "\",\"baseSalary\":}"))
                .andExpect(status().isBadRequest())
                .andExpect(header().string("X-Correlation-ID", "b21541a5-e09e-442c-a736-bf42819d6d2c"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Payload inválido"))
                .andExpect(jsonPath("$.errors[0]").value("Verifique o formato dos dados enviados"))
                .andExpect(content().string(not(containsString(sensitiveValue))));

        assertSanitizedLogs(sensitiveValue);
    }

    @Test
    void shouldSanitizeUnexpectedExceptionResponse() throws Exception {
        String sensitiveValue = "employeeId=CPF-12345678900";
        when(service.validate(any())).thenThrow(new IllegalStateException(sensitiveValue));

        mockMvc.perform(post("/api/payroll/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Correlation-ID", "not-a-uuid")
                        .content("""
                                {
                                  "employeeId": "EMP001",
                                  "baseSalary": 1000.00,
                                  "workedHours": 160,
                                  "overtimeHours": 2,
                                  "discounts": [],
                                  "period": "2026-03"
                                }
                                """))
                .andExpect(status().isInternalServerError())
                .andExpect(header().string("X-Correlation-ID", matchesPattern("[0-9a-f-]{36}")))
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.message").value("Erro inesperado"))
                .andExpect(jsonPath("$.errors[0]").value("Não foi possível concluir a solicitação"))
                .andExpect(content().string(not(containsString(sensitiveValue))));

        assertSanitizedLogs(sensitiveValue);
    }

    @Test
    void shouldGenerateCorrelationIdWhenHeaderIsMissing() throws Exception {
        mockMvc.perform(post("/api/payroll/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"baseSalary\":}"))
                .andExpect(status().isBadRequest())
                .andExpect(header().string("X-Correlation-ID", matchesPattern("[0-9a-f-]{36}")));
    }

    @Test
    void shouldReplaceParseableButNonCanonicalCorrelationId() throws Exception {
        mockMvc.perform(post("/api/payroll/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Correlation-ID", "1-1-1-1-1")
                        .content("{\"baseSalary\":}"))
                .andExpect(status().isBadRequest())
                .andExpect(header().string("X-Correlation-ID", matchesPattern("[0-9a-f-]{36}")))
                .andExpect(header().string("X-Correlation-ID", not("1-1-1-1-1")));
    }

    private void assertSanitizedLogs(String sensitiveValue) {
        assertFalse(logHandler.list.isEmpty());
        logHandler.list.forEach(record -> {
            assertFalse(record.getFormattedMessage().contains(sensitiveValue));
            assertNull(record.getThrowableProxy());
        });
    }
}
