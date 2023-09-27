package com.getjoystick.sdk.errors;

import lombok.experimental.StandardException;

/**
 * Base exception class for the whole SDK exceptions
 */
@StandardException
public class JoystickException extends RuntimeException {
    private static final long serialVersionUID = -1L;

}
