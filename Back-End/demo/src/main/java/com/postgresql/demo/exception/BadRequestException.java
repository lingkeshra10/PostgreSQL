package com.postgresql.demo.exception;

import com.postgresql.demo.modal.ResponseModal;

public class BadRequestException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final int code;

    /**
     * Creates an exception using the message mapped to the response code.
     */
    public BadRequestException(int code) {
        super(ResponseModal.getResponseMsg(code));
        this.code = code;
    }

    /**
     * Creates an exception with a custom message.
     * Useful when the message contains dynamic information.
     */
    public BadRequestException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}