package com.getjoystick.sdk.models;

import com.getjoystick.sdk.util.JoystickMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JoystickFullContentTest {

    @Test
    void testHashCode() {
        final JoystickFullContent content =
            new JoystickFullContent("{\"data\":{\"level\":133,\"mode\":\"Hard\",\"age\":18,\"price\":33.99}," +
                "\"hash\":\"e10325c5\",\"meta\":{\"uid\":0,\"mod\":0,\"variants\":[],\"seg\":[]}}", false);
        assertEquals(-1245507023, content.hashCode());
    }

    @Test
    void testEquals() {
        final JoystickFullContent content =
            new JoystickFullContent("{\"data\":{\"level\":133,\"mode\":\"Hard\",\"age\":18,\"price\":33.99}," +
                "\"hash\":\"e10325c5\",\"meta\":{\"uid\":0,\"mod\":0,\"variants\":[],\"seg\":[]}}", false);
        final JoystickFullContent duplicateContent =
            new JoystickFullContent("{\"data\":{\"level\":133,\"mode\":\"Hard\",\"age\":18,\"price\":33.99}," +
                "\"hash\":\"e10325c5\",\"meta\":{\"uid\":0,\"mod\":0,\"variants\":[],\"seg\":[]}}", false);
        final JoystickFullContent otherContent =
            new JoystickFullContent(JoystickMapper.readTree("{\"data\":{\"level\":133,\"mode\":\"Hard\"," +
                "\"age\":18,\"price\":11.11}," +
                "\"hash\":\"e10325c5\",\"meta\":{\"uid\":0,\"mod\":0,\"variants\":[],\"seg\":[]}}"),
                "{\"data\":{\"level\":133,\"mode\":\"Hard\",\"age\":18,\"price\":11.11}," +
                "\"hash\":\"e10325c5\",\"meta\":{\"uid\":0,\"mod\":0,\"variants\":[],\"seg\":[]}}",
                new JoystickMeta(), "123234");
        final JoystickFullContent nullContent = new JoystickFullContent();
        nullContent.setData(null);
        nullContent.setHash(null);
        nullContent.setMeta(null);
        assertTrue(content.equals(content));
        assertTrue(content.equals(duplicateContent));
        assertFalse(content.equals(otherContent));
        assertFalse(nullContent.equals(otherContent));
        assertFalse(content.equals(nullContent));
        assertFalse(content.equals(null));

        duplicateContent.setHash(null);
        assertFalse(duplicateContent.equals(content));
        assertFalse(content.equals(duplicateContent));
        duplicateContent.setMeta(null);
        assertFalse(duplicateContent.equals(content));
        assertFalse(content.equals(duplicateContent));
        duplicateContent.setData(null);
        assertFalse(duplicateContent.equals(content));
        assertFalse(content.equals(duplicateContent));
    }
}
