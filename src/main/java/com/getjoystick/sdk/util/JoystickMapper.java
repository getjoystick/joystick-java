package com.getjoystick.sdk.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class JoystickMapper {
    private static final ObjectMapper OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = JsonMapper.builder()
            .enable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY)
            .serializationInclusion(JsonInclude.Include.NON_NULL)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .build();
    }

    JoystickMapper() {
        throw new IllegalStateException("Utility class.");
    }

    public static  <T> T readValue(final String content, final Class<T> valueType)
        throws JsonProcessingException {
        return OBJECT_MAPPER.readValue(content, valueType);
    }

    public static  <T> T treeToValue(final TreeNode node, final Class<T> valueType)
        throws IllegalArgumentException, JsonProcessingException
    {
        return OBJECT_MAPPER.treeToValue(node, valueType);
    }

    public static JsonNode readTree(final InputStream inputStream) throws IOException
    {
        return OBJECT_MAPPER.readTree(inputStream);
    }

    public static JsonNode readTree(final String input) throws JsonProcessingException {
        return OBJECT_MAPPER.readTree(input);
    }

    public static void writeValue(OutputStream outputStream, Object value)
        throws IOException {
        OBJECT_MAPPER.writeValue(outputStream, value);
    }

    public static String writeValueAsString(Object value) throws JsonProcessingException{
        return OBJECT_MAPPER.writeValueAsString(value);
    }
}
