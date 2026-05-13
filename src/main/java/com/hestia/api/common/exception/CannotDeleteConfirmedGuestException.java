package com.hestia.api.common.exception;

public class CannotDeleteConfirmedGuestException extends RuntimeException {

    public CannotDeleteConfirmedGuestException() {
        super("Cannot delete guest already confirmed");
    }
}
