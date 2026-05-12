package com.hestia.api.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class CannotDeleteInviteWithConfirmedGuestsException extends RuntimeException {

    public CannotDeleteInviteWithConfirmedGuestsException() {
        super("Cannot delete invite with confirmed guests");
    }
}
