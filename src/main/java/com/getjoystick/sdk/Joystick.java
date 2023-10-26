package com.getjoystick.sdk;

import com.getjoystick.sdk.client.Client;
import com.getjoystick.sdk.client.ClientConfig;
import com.getjoystick.sdk.client.impl.ClientImpl;

/**
 * Main SDK class.
 */
public final class Joystick {

    /**
     * Create new instance of Joystick Client
     *
     * @param config Client configuration
     * @return Client to communicate with Joystick
     */
    public static Client create(final ClientConfig config) {
        return new ClientImpl(config);
    }

    /* default */ Joystick() {
        throw new UnsupportedOperationException("Unable to instantiate a Base SDK class.");
    }

}
