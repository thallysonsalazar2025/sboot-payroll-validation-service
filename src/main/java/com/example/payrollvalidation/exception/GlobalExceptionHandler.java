package com.example.payrollvalidation.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestControllerAdvice
public class GlobalExceptionHandler {

    static final String CORRELATION_ID_HEADER = "X-Correlation-ID";
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final String INVALID_PAYLOAD_DETAIL = "Verifique o formato dos dados enviados";
    private static final String INTERNAL_ERROR_DETAIL = "Não foi possível concluir a solicitação";

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpServletRequest request
    ) {
        String correlationId = correlationId(request);
        LOGGER.warn("Falha na requisição. correlationId={}, exceptionType={}",
                correlationId, ex.getClass().getSimpleName());
        ApiErrorResponse error = new ApiErrorResponse(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Payload inválido",
                List.of(INVALID_PAYLOAD_DETAIL)
        );
        return ResponseEntity.badRequest()
                .header(CORRELATION_ID_HEADER, correlationId)
                .body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGenericException(Exception ex, HttpServletRequest request) {
        String correlationId = correlationId(request);
        LOGGER.error("Falha na requisição. correlationId={}, exceptionType={}",
                correlationId, ex.getClass().getSimpleName());
        ApiErrorResponse error = new ApiErrorResponse(
                Instant.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Erro inesperado",
                List.of(INTERNAL_ERROR_DETAIL)
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header(CORRELATION_ID_HEADER, correlationId)
                .body(error);
    }

    private static String correlationId(HttpServletRequest request) {
        String received = request.getHeader(CORRELATION_ID_HEADER);
        if (received != null) {
            try {
                UUID parsed = UUID.fromString(received);
                if (parsed.toString().equalsIgnoreCase(received)) {
                    return parsed.toString();
                }
            } catch (IllegalArgumentException ignored) {
                // Invalid external values are intentionally replaced and never logged.
            }
        }
        return UUID.randomUUID().toString();
    }
}
