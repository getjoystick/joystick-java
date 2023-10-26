package com.getjoystick.sdk.errors;

public class ConfigurationException extends JoystickException {
    private static final long serialVersionUID = -1L;

    public ConfigurationException(final String message) {
        this(message, null);
    }

    public ConfigurationException(final Throwable cause) {
        this(cause != null ? cause.getMessage() : null, cause);
    }

    public ConfigurationException(final String message, final Throwable cause) {
        super(message);
        if (cause != null) {
            super.initCause(cause);
        }
    }

}
