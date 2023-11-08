package com.getjoystick.sdk.util;

import com.fasterxml.jackson.core.JsonParseException;
import com.getjoystick.sdk.errors.JoystickException;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class JoystickMapperTest {

    @Test
    void constructor_exceptionIsThrown() {
        final IllegalStateException exc = assertThrows(IllegalStateException.class,
            () -> new JoystickMapper());
        assertEquals( "Utility class.", exc.getMessage());
    }

    @Test
    void readValue_invalidInput_exceptionIsThrown() {
        final JoystickException exc = assertThrows(JoystickException.class,
            () -> JoystickMapper.readValue("sdfsdfsdf", Object.class));
        assertEquals( "Unable to convert Joystick response to class java.lang.Object", exc.getMessage());
    }

    @Test
    void treeToValue_invalidInput_exceptionIsThrown() {
        final JoystickException exc = assertThrows(JoystickException.class,
            () -> JoystickMapper.treeToValue(
                JoystickMapper.readTree("{\"speed\":20,\"name\":\"Turbo\",\"size\":245,\"price\":22.99}"),
                Integer.class));
        assertEquals( "Unable to convert Joystick response to class java.lang.Integer", exc.getMessage());
    }

    @Test
    void readTree_invalidInput_exceptionIsThrown() {
        final JoystickException exc = assertThrows(JoystickException.class,
            () -> JoystickMapper.readTree("invalid json"));
        assertEquals( "Unable to convert Joystick response to JsonNode", exc.getMessage());
    }

    @Test
    void testReadTree_invalidInput_exceptionIsThrown() {
        assertThrows(JsonParseException.class,
            () -> JoystickMapper.readTree(new ByteArrayInputStream("invalid json".getBytes(StandardCharsets.UTF_8))));
    }

}
