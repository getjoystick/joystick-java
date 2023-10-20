package com.getjoystick.sdk.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.getjoystick.sdk.util.JoystickMapper;
import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class JoystickData {

    private JsonNode data;

    public <T> T asObject(final Class<T> clazz) throws JsonProcessingException {
        return JoystickMapper.treeToValue(data, clazz);
    }

    public JsonNode asJson() {
        return data;
    }

}
