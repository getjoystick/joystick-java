package com.getjoystick.sdk.models;

import com.fasterxml.jackson.databind.JsonNode;
import com.getjoystick.sdk.util.JoystickMapper;

import java.util.Objects;

public class JoystickContent {

    private final JsonNode content;

    public JoystickContent(final JsonNode content) {
        this.content = content;
    }

    public <T> T asObject(final Class<T> clazz) {
        return JoystickMapper.treeToValue(content, clazz);
    }

    public JsonNode asJson() {
        return content;
    }

    public JsonNode get(final String nodeName) {
        return content.get(nodeName);
    }

    public JsonNode get(final int index) {
        return content.get(index);
    }
    @Override
    public String toString() {
        return content != null ? JoystickMapper.removeTrailingQuotes(content.toString()) : "";
    }

    @Override
    public int hashCode() {
        return Objects.hash(content);
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof JoystickContent)) {
            return false;
        }

        final JsonNode thisContent = this.content;
        final JsonNode otherContent = ((JoystickContent) other).content;
        if (thisContent == null) {
            return otherContent == null;
        }
        return thisContent.equals(otherContent);
    }
}
