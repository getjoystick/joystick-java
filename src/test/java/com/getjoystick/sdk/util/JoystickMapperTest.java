package com.getjoystick.sdk.util;

import com.fasterxml.jackson.core.JsonParseException;
import com.getjoystick.sdk.errors.JoystickException;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JoystickMapperTest {

    @Test
    void constructor_exceptionIsThrown() {
        final IllegalStateException exc = assertThrows(IllegalStateException.class,
            () -> new JoystickMapper());
        assertEquals( "Utility class.", exc.getMessage());
    }

    @Test
    void toObject_validInput_jsonConvertedToSpecifiedObject() {
        final Map result = JoystickMapper.toObject(
                JoystickUtil.readTree("{\"speed\":20,\"name\":\"Turbo\",\"size\":245,\"price\":22.99}"),
                Map.class);
        assertEquals( 20, result.get("speed"));
        assertEquals( "Turbo", result.get("name"));
        assertEquals( 245, result.get("size"));
        assertEquals( 22.99, result.get("price"));
    }

    @Test
    void toObject_invalidInput_exceptionIsThrown() {
        final JoystickException exc = assertThrows(JoystickException.class,
            () -> JoystickMapper.toObject(
                JoystickUtil.readTree("{\"speed\":20,\"name\":\"Turbo\",\"size\":245,\"price\":22.99}"),
                Integer.class));
        assertEquals( "Unable to convert Joystick response to class java.lang.Integer", exc.getMessage());
    }

}
