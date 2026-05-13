package com.hestia.api.common.exception;

public class CannotDeleteInviteWithConfirmedGuestsException extends RuntimeException {

    public CannotDeleteInviteWithConfirmedGuestsException() {
        super("Cannot delete invite with confirmed guests");
    }
}
