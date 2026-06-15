package com.hestia.api.domain.registry.exception;

public class InsufficientStockReductionException extends RuntimeException {

    public InsufficientStockReductionException(String message) {
        super(message);
    }
}
