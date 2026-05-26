package com.hestia.api.domain.payment.exception;

public class DuplicatePaymentConfigException extends RuntimeException {

    public DuplicatePaymentConfigException(String message) {
        super(message);
    }
}
