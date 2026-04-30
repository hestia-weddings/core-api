package com.hestia.api.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class CannotDeleteConfirmedInviteException extends RuntimeException {

    public CannotDeleteConfirmedInviteException() {
        super("Cannot delete invite already confirmed");
    }
}
