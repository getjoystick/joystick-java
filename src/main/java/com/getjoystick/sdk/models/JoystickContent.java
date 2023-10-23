package com.getjoystick.sdk.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.getjoystick.sdk.util.JoystickMapper;

public class JoystickContent {

    private JsonNode content;

    public JoystickContent(final JsonNode content) {
        this.content = content;
    }

    public <T> T asObject(final Class<T> clazz) throws JsonProcessingException {
        return JoystickMapper.treeToValue(content, clazz);
    }

    public JsonNode asJson() {
        return content;
    }

    public JoystickContent get(final String nodeName) {
        final JsonNode childNode = content.get(nodeName);
        return childNode == null ? null : new JoystickContent(childNode);
    }

    public JoystickContent get(final int index) {
        final JsonNode childNode = content.get(index);
        return childNode == null ? null : new JoystickContent(childNode);
    }

    public String toString() {
        return "JoystickData(data=" + this.content + ")";
    }

}
