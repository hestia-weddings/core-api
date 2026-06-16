package com.hestia.api.common.exception;

import com.hestia.api.common.dto.ErrorResponse;
import com.hestia.api.domain.payment.exception.DuplicateWalletException;
import com.hestia.api.domain.payment.exception.InsufficientBalanceException;
import com.hestia.api.domain.registry.exception.InsufficientStockReductionException;
import com.hestia.api.infrastructure.asaas.exception.AsaasCheckoutException;
import com.hestia.api.infrastructure.asaas.exception.AsaasTransferException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handle(ResourceNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler({
        CannotDeleteInviteWithConfirmedGuestsException.class,
        CannotDeleteConfirmedGuestException.class,
        CannotDeleteLinkedMessageException.class,
        DuplicateWalletException.class
    })
    public ResponseEntity<ErrorResponse> handleConflict(RuntimeException ex) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(IllegalArgumentException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> fields = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(e -> fields.put(e.getField(), e.getDefaultMessage()));

        ErrorResponse body = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Validation failed")
                .message("One or more fields are invalid")
                .fields(fields)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(AsaasCheckoutException.class)
    public ResponseEntity<ErrorResponse> handleAsaasError(AsaasCheckoutException ex) {
        return buildResponse(HttpStatus.BAD_GATEWAY, ex.getMessage());
    }

    @ExceptionHandler(AsaasTransferException.class)
    public ResponseEntity<ErrorResponse> handleAsaasTransferError(AsaasTransferException ex) {
        return buildResponse(HttpStatus.BAD_GATEWAY, ex.getMessage());
    }

    @ExceptionHandler({InsufficientBalanceException.class, InsufficientStockReductionException.class})
    public ResponseEntity<ErrorResponse> handleUnprocessableEntity(RuntimeException ex) {
        return buildResponse(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
    }

    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String message) {
        ErrorResponse body = ErrorResponse.builder()
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(status).body(body);
    }
}
