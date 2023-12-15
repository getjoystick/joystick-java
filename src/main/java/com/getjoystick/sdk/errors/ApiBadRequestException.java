package com.getjoystick.sdk.errors;

public class ApiBadRequestException extends ApiHttpException {
    private static final long serialVersionUID = -1L;

    public ApiBadRequestException(final String message) {
        super(message);
    }

}
