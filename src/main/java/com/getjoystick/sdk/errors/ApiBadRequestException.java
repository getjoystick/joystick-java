package com.getjoystick.sdk.errors;

public class ApiBadRequestException extends ApiHttpException {
    private static final long serialVersionUID = -1L;

    public ApiBadRequestException(final String message) {
        this(message, null);
    }

    public ApiBadRequestException(final String message, final Throwable cause) {
        super(message);
        if (cause != null) {
            super.initCause(cause);
        }
    }

}
