package com.hestia.api.common.exception;

public class CannotDeleteLinkedMessageException extends RuntimeException {

    public CannotDeleteLinkedMessageException() {
        super("Cannot delete message linked to an order or invite");
    }
}
