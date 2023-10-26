package com.getjoystick.sdk.errors;

public class MultipleContentsApiException extends ApiException {
    private static final long serialVersionUID = -1L;

    public MultipleContentsApiException(final String message) {
        this(message, null);
    }

    public MultipleContentsApiException(final String message, final Throwable cause) {
        super(message);
        if (cause != null) {
            super.initCause(cause);
        }
    }

}
