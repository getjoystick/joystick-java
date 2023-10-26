package com.getjoystick.sdk.errors;

public class ApiHttpException extends JoystickException {
    private static final long serialVersionUID = -1L;

    public ApiHttpException(final String message) {
        this(message, null);
    }

    public ApiHttpException(final String message, final Throwable cause) {
        super(message);
        if (cause != null) {
            super.initCause(cause);
        }
    }

}
