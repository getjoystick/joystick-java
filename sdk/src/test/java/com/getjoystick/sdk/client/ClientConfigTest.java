package com.getjoystick.sdk.client;

import com.getjoystick.sdk.errors.ConfigurationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ClientConfigTest {

    private static final String API_KEY = "test-api-key";

    @Test
    @DisplayName("CN-04")
    void testIfAPIKeyIsProvided() {
        final ClientConfig cfg = ClientConfig.builder().setApiKey(API_KEY).build();
        assertNotNull(cfg);
        assertEquals(API_KEY, cfg.getApiKey());
    }

    @Test
    @DisplayName("CN-04")
    void testIfAPIKeyIsNotProvided() {
        final ConfigurationException error =
            assertThrows(ConfigurationException.class, () -> ClientConfig.builder().build());
        assertEquals("API key is not provided.", error.getMessage());
    }

    @Test
    @DisplayName("CCVD-01")
    void testIfAPIKeyIsEmpty() {
        final ConfigurationException error =
            assertThrows(ConfigurationException.class, () -> ClientConfig.builder().setApiKey("        ").build());
        assertEquals("API key is not provided.", error.getMessage());
    }

    @Test
    void testIfSemanticVersionIsValid() {
        final String[] validSemanticVersions = new String[]{"0.0.4", "1.2.3", "10.20.30", "1.0.0", "2.0.0", "1.1.7" };
        for (final String semVer : validSemanticVersions) {
            final ClientConfig cfg = ClientConfig.builder().setApiKey(API_KEY).setSemVer(semVer).build();
            assertNotNull(cfg);
        }
    }

    @Test
    void testIfSemanticVersionIsInvalid_1() {
        final ConfigurationException error =
            assertThrows(ConfigurationException.class,
                () -> ClientConfig.builder().setApiKey(API_KEY).setSemVer("0.1").build());
        assertEquals("The semantic version [0.1] is incorrect.", error.getMessage());
    }

    @Test
    void testIfSemanticVersionIsInvalid_2() {
        final ConfigurationException error =
            assertThrows(ConfigurationException.class,
                () -> ClientConfig.builder().setApiKey(API_KEY).setSemVer("0.0.1-beta.1").build());
        assertEquals("The semantic version [0.0.1-beta.1] is incorrect.", error.getMessage());
    }

    @Test
    @DisplayName("CCVD-05 - FAILED")
    void testIfSetPositiveCacheExpirationInSeconds() {
        final ConfigurationException error =
            assertThrows(ConfigurationException.class,
                () -> ClientConfig.builder().setCacheTTL(-1).build());
        assertEquals("Cache expiration time must be defined as seconds and must be positive.",
            error.getMessage());
    }

    @Test
    @DisplayName("CCVD-05 - OK")
    void testIfSetNegativeCacheExpirationInSeconds() {
        final ClientConfig cfg = ClientConfig.builder().setApiKey(API_KEY).setCacheTTL(0).build();
        assertNotNull(cfg);
        assertEquals(0, cfg.getCacheTTL());
    }

    @Test
    @DisplayName("CCVD-06 - OK-1")
    void testIfSerializedPropertyIsBooleanTrue() {
        final ClientConfig cfg = ClientConfig.builder().setApiKey(API_KEY).setSerialized(true).build();
        assertNotNull(cfg);
        assertTrue(cfg.isSerialized());
    }

    @Test
    @DisplayName("CCVD-06 - OK-2")
    void testIfSerializedPropertyIsBooleanFalse() {
        final ClientConfig cfg = ClientConfig.builder().setApiKey(API_KEY).build();
        assertNotNull(cfg);
        assertFalse(cfg.isSerialized());
    }

}
