package com.hestia.api.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class CannotDeleteHouseholdWithConfirmedInvitesException extends RuntimeException {

    public CannotDeleteHouseholdWithConfirmedInvitesException() {
        super("Cannot delete household with confirmed invites");
    }
}
