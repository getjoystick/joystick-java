package com.getjoystick.sdk.models;

/**
 * Class to store optional parameters for loading Joystick content
 */
public class JoystickContentOptions {

    /**
     * If true, then content is loaded via Joystick API remote call, skipping cache.
     */
    private boolean refresh;

    public JoystickContentOptions(final boolean refresh) {
        this.refresh = refresh;
    }

    public boolean isRefresh() {
        return refresh;
    }

}
