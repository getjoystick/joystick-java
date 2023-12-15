package com.getjoystick.sdk.errors;

public class ConfigurationException extends JoystickException {
    private static final long serialVersionUID = -1L;

    public ConfigurationException(final String message) {
        super(message);
    }

    public ConfigurationException(final String message, final Throwable cause) {
        super(message);
        super.initCause(cause);
    }

}
