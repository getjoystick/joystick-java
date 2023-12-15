package com.getjoystick.sdk.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.getjoystick.sdk.errors.JoystickException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class JoystickUtil {
    private static final ObjectMapper OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = JsonMapper.builder()
            .enable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY)
            .serializationInclusion(JsonInclude.Include.NON_NULL)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .build();
    }

    /* default */ JoystickUtil() {
        throw new IllegalStateException("Utility class.");
    }

    public static  <T> T treeToValue(final TreeNode node, final Class<T> clazz) {
        try {
            return OBJECT_MAPPER.treeToValue(node, clazz);
        } catch (JsonProcessingException e) {
            throw new JoystickException("Unable to convert Joystick response to " + clazz, e);
        }
    }

    public static JsonNode readTree(final InputStream inputStream) throws IOException
    {
        return OBJECT_MAPPER.readTree(inputStream);
    }

    public static JsonNode readTree(final String input) {
        try {
            return OBJECT_MAPPER.readTree(input);
        } catch (JsonProcessingException e) {
            throw new JoystickException("Unable to convert Joystick response to JsonNode", e);
        }
    }

    public static void writeValue(final OutputStream outputStream, final Object value)
        throws IOException {
        OBJECT_MAPPER.writeValue(outputStream, value);
    }

    public static String writeValueAsString(final Object value) throws JsonProcessingException{
        return OBJECT_MAPPER.writeValueAsString(value);
    }

    public static String removeTrailingQuotes(final String inputString) {
        if (inputString != null && inputString.length() >= 2
            && inputString.charAt(0) == '\"' && inputString.charAt(inputString.length() - 1) == '\"') {
            return inputString.substring(1, inputString.length() - 1);
        }
        return inputString;
    }
}
