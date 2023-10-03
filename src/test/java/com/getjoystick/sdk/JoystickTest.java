package com.getjoystick.sdk;

import com.getjoystick.sdk.client.Client;
import com.getjoystick.sdk.client.ClientConfig;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JoystickTest {

    private static final String API_KEY = "test-api-key";

    @Test
    @Tag("CN-01")
    void create_testSingleClientInstantiation_success() {
        final Client client = Joystick.create(ClientConfig.builder().setApiKey(API_KEY).setSemVer("0.0.1").build());
        assertNotNull(client);
    }

    @Test
    @Tag("CN-01")
    void create_multipleClientsWithDifferentConfigs_statesAreNotShared() {
        final Client client1 = Joystick.create(ClientConfig.builder().setApiKey(API_KEY).setSemVer("0.0.1").build());
        final Client client2 = Joystick.create(ClientConfig.builder().setApiKey(API_KEY).setSemVer("0.0.2").build());
        assertNotSame(client1, client2);
    }

    @Test
    @Tag("CN-02")
    void create_multipleClientsWithSameConfigs_statesAreNotShared() {
        final Client client1 = Joystick.create(ClientConfig.builder().setApiKey(API_KEY).setSemVer("0.0.1").build());
        final Client client2 = Joystick.create(ClientConfig.builder().setApiKey(API_KEY).setSemVer("0.0.1").build());
        assertNotSame(client1, client2);
    }
    @Test
    void constructor_default_unsupportedOperation() {
        assertThrows(UnsupportedOperationException.class, Joystick::new);
    }
}
