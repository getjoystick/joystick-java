package com.getjoystick.sdk.errors;

public class ApiHttpException extends JoystickException {
    private static final long serialVersionUID = -1L;

    public ApiHttpException(final String message) {
        super(message);
    }

}
