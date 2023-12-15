package com.getjoystick.sdk.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.getjoystick.sdk.client.ClientConfig;
import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public final class ApiCacheKeyUtil {

    /**
     * GCSC-01
     * Implementation of SHA-256 hash algorithm to calculate configuration hash.
     *
     * @param config client configuration
     * @param contentIds collection of content ids
     * @param serialized if true, then result will be returned in serialized format
     * @param fullResponse if true - return full content, including data, meta and hash
     * @return string hash based on input parameters
     */
    public static String getHash(final ClientConfig config, final Collection<String> contentIds,
                                 final boolean serialized, final boolean fullResponse) {
        final String contentIdsString = contentIds.stream().sorted().collect(Collectors.joining(","));
        return getHash(config, contentIdsString, serialized, fullResponse);
    }

    /**
     * GCSC-01
     * Implementation of SHA-256 hash algorithm to calculate configuration hash.
     *
     * @param config client configuration
     * @param contentIdsString content id or ids in string format
     * @param serialized if true, then result will be returned in serialized format
     * @param fullResponse if true - return full content, including data, meta and hash
     * @return string hash based on input parameters
     */
    public static String getHash(final ClientConfig config, final String contentIdsString,
                                 final boolean serialized, final boolean fullResponse) {
        final String key = config.getApiKey();
        final Map<Object, Object> props = new TreeMap<>(config.getParams());
        String propsString;
        try {
            propsString = JoystickUtil.writeValueAsString(props);
        } catch (JsonProcessingException e) {
            propsString = String.valueOf(props);
        }
        final String semVer = config.getSemVer();
        final String userId = config.getUserId();
        final String [] hashArray = {key, propsString, semVer, userId, contentIdsString,
            Boolean.toString(serialized), Boolean.toString(fullResponse)};
        String hashString;
        try {
            hashString = JoystickUtil.writeValueAsString(hashArray);
        } catch (JsonProcessingException e) {
            hashString = Arrays.toString(hashArray);
        }
        return Hashing.sha256().hashString(hashString, StandardCharsets.UTF_8).toString();
    }

    /* default */ ApiCacheKeyUtil() {
        throw new IllegalStateException("Utility class.");
    }

}
