package com.getjoystick.sdk.util;

import com.google.common.hash.Hashing;
import com.getjoystick.sdk.client.ClientConfig;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public final class ApiCacheKeyUtil {

    /**
     * GCSC-01
     * Implementation of SHA-256 hash algorithm to calculate configuration hash.
     *
     * @param config
     * @param contentIds
     * @param serialized
     * @param fullResponse
     * @return
     */
    public static String getHash(
        final ClientConfig config, final Collection<String> contentIds, final boolean serialized, final boolean fullResponse
    ) {
        final String key = config.getApiKey();
        final Map<Object, Object> props = new TreeMap<>(config.getParams());
        final String propsKey = props.entrySet().stream()
            .map(e -> e.getKey().toString() + e.getValue().toString())
            .collect(Collectors.joining());
        final String semVer = config.getSemVer();
        final String userId = config.getUserId();
        final String contentIdsString = contentIds.stream().sorted().collect(Collectors.joining());
        final String hashString = key + propsKey + semVer + userId + contentIdsString + serialized + fullResponse;
        return Hashing.sha256().hashString(hashString, StandardCharsets.UTF_8).toString();
    }

    ApiCacheKeyUtil() {
        throw new IllegalStateException("Utility class.");
    }

}
