package com.hestia.api.domain.payment.exception;

public class DuplicateWalletException extends RuntimeException {

    public DuplicateWalletException(String message) {
        super(message);
    }
}
