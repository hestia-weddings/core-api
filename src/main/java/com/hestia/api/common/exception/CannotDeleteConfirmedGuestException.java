package com.hestia.api.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class CannotDeleteConfirmedGuestException extends RuntimeException {

    public CannotDeleteConfirmedGuestException() {
        super("Cannot delete guest already confirmed");
    }
}
