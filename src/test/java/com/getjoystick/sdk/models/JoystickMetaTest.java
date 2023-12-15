package com.getjoystick.sdk.models;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JoystickMetaTest {

    @Test
    void testToString() {
        final JoystickMeta joystickMeta =
            new JoystickMeta(1L, 1L, ImmutableList.of("one", "two"), ImmutableList.of("three", "four"));
        assertEquals("JoystickMeta(uid=1, mod=1, variants=[one, two], seg=[three, four])", joystickMeta.toString());
    }

    @Test
    void testHashCode() {
        final JoystickMeta joystickMeta =
            new JoystickMeta(1L, 1L, ImmutableList.of("one", "two"), ImmutableList.of("three", "four"));
        assertEquals(-650586093, joystickMeta.hashCode());
    }

    @Test
    void testEquals() {
        final JoystickMeta joystickMeta =
            new JoystickMeta(1L, 1L, ImmutableList.of("one", "two"), ImmutableList.of("three", "four"));
        final JoystickMeta duplicateContent =
            new JoystickMeta(1L, 1L, ImmutableList.of("one", "two"), ImmutableList.of("three", "four"));
        final JoystickMeta otherContent =
            new JoystickMeta(1L, 1L, ImmutableList.of("one", "two"), ImmutableList.of("three", "five"));
        final JoystickMeta nullContent =
            new JoystickMeta(null, null, null, null);
        assertTrue(joystickMeta.equals(joystickMeta));
        assertTrue(joystickMeta.equals(duplicateContent));
        assertFalse(joystickMeta.equals(otherContent));
        assertFalse(nullContent.equals(otherContent));
        assertFalse(joystickMeta.equals(nullContent));
        assertFalse(joystickMeta.equals(null));

        duplicateContent.setSeg(null);
        assertFalse(duplicateContent.equals(joystickMeta));
        assertFalse(joystickMeta.equals(duplicateContent));
        duplicateContent.setVariants(null);
        assertFalse(duplicateContent.equals(joystickMeta));
        assertFalse(joystickMeta.equals(duplicateContent));
        duplicateContent.setMod(null);
        assertFalse(duplicateContent.equals(joystickMeta));
        assertFalse(joystickMeta.equals(duplicateContent));
        duplicateContent.setUid(null);
        assertFalse(duplicateContent.equals(joystickMeta));
        assertFalse(joystickMeta.equals(duplicateContent));
    }

}
