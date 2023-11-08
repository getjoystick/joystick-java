package com.getjoystick.sdk.errors;

public class ApiException extends JoystickException {
    private static final long serialVersionUID = -1L;

    public ApiException(final String message) {
        super(message);
    }

}
