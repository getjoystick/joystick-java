package com.getjoystick.sdk.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.getjoystick.sdk.util.JoystickMapper;
import lombok.Data;


@Data
public class JoystickFullContentJson {

    private JsonNode data;

    private JoystickMeta meta;

    private String hash;

    public <T> T getData(final Class<T> clazz) throws JsonProcessingException {
        return JoystickMapper.treeToValue(data, clazz);
    }
}
