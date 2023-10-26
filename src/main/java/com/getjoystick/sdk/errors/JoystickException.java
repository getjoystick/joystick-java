package com.getjoystick.sdk.errors;

/**
 * Base exception class for the whole SDK exceptions
 */
public class JoystickException extends RuntimeException {
    private static final long serialVersionUID = -1L;

    public JoystickException(final String message) {
        this(message, null);
    }

    public JoystickException(final String message, final Throwable cause) {
        super(message);
        if (cause != null) {
            super.initCause(cause);
        }
    }

}
