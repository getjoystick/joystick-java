package com.getjoystick.sdk.errors;

public class ApiServerException extends ApiHttpException {
    private static final long serialVersionUID = -1L;

    public ApiServerException(final String message) {
        super(message);
    }

}
