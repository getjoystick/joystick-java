package com.getjoystick.sdk.errors;

public class ApiUnknownException extends ApiHttpException {
    private static final long serialVersionUID = -1L;

    public ApiUnknownException(final String message) {
        super(message);
    }

    public ApiUnknownException(final String message, final Throwable cause) {
        super(message);
        if (cause != null) {
            super.initCause(cause);
        }
    }

}
