package com.getjoystick.sdk.client.endpoints;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApiEndpointFactoryTest {

    @Test
    void construct_apiEndpointFactory_exceptionIsThrown() {
        assertThrows(IllegalStateException.class, ApiEndpointFactory::new);
    }

}
