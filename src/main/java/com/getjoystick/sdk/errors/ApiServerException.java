package com.getjoystick.sdk.errors;

public class ApiServerException extends ApiHttpException {
    private static final long serialVersionUID = -1L;

    public ApiServerException(final String message) {
        this(message, null);
    }

    public ApiServerException(final String message, final Throwable cause) {
        super(message);
        if (cause != null) {
            super.initCause(cause);
        }
    }

}
