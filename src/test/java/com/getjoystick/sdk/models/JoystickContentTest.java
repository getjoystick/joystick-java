package com.getjoystick.sdk.models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.getjoystick.sdk.util.JoystickMapper;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JoystickContentTest {

    @Test
    void testGet() {
        final JoystickContent content =
            new JoystickContent(JoystickMapper.readTree("{\"speed\":20,\"name\":\"Turbo\",\"size\":245,\"price\":22.99}"));
        JsonNode zeroNode =  content.get(0);
        JsonNode nameNode =  content.get("name");
        assertEquals(new TextNode("Turbo"),
            nameNode);
        assertNull(zeroNode);
    }

    @Test
    void asObject() {
        final JoystickContent content =
            new JoystickContent(JoystickMapper.readTree("{\"speed\":20,\"name\":\"Turbo\",\"size\":245,\"price\":22.99}"));
        Map<String, Object> objectMap = content.asObject(Map.class);
        assertEquals(4, objectMap.size());
        assertEquals("Turbo", objectMap.get("name"));
        assertEquals(20, objectMap.get("speed"));
        assertEquals(245, objectMap.get("size"));
        assertEquals(22.99, objectMap.get("price"));
    }

    @Test
    void asJson() {
        final JoystickContent content =
            new JoystickContent(JoystickMapper.readTree("{\"speed\":20,\"name\":\"Turbo\",\"size\":245,\"price\":22.99}"));
        JsonNode jsonNode = content.asJson();
        assertEquals(jsonNode, JoystickMapper.readTree("{\"speed\":20,\"name\":\"Turbo\",\"size\":245,\"price\":22.99}"));
    }

    @Test
    void testToString() {
        final JoystickContent content =
            new JoystickContent(JoystickMapper.readTree("{\"speed\":20,\"name\":\"Turbo\",\"size\":245,\"price\":22.99}"));
        assertEquals("{\"speed\":20,\"name\":\"Turbo\",\"size\":245,\"price\":22.99}", content.toString());
    }

    @Test
    void testHashCode() {
        final JoystickContent content =
            new JoystickContent(JoystickMapper.readTree("{\"speed\":20,\"name\":\"Turbo\",\"size\":245,\"price\":22.99}"));
        assertEquals(-245166369, content.hashCode());
    }

    @Test
    void testEquals() {
        final JoystickContent content =
            new JoystickContent(JoystickMapper.readTree("{\"speed\":20,\"name\":\"Turbo\",\"size\":245,\"price\":22.99}"));
        final JoystickContent duplicateContent =
            new JoystickContent(JoystickMapper.readTree("{\"speed\":20,\"name\":\"Turbo\",\"size\":245,\"price\":22.99}"));
        final JoystickContent otherContent =
            new JoystickContent(JoystickMapper.readTree("{\"speed\":55,\"name\":\"JJJ\",\"size\":0,\"price\":25.99}"));
        final JoystickContent nullContent =
            new JoystickContent(null);
        assertTrue(content.equals(content));
        assertTrue(content.equals(duplicateContent));
        assertFalse(content.equals(otherContent));
        assertFalse(nullContent.equals(otherContent));
        assertFalse(content.equals(nullContent));
        assertFalse(content.equals(null));
    }
}
