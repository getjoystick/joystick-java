package com.getjoystick.sdk.client.endpoints;

import com.getjoystick.sdk.client.ClientConfig;

import java.util.Collection;

public class ApiEndpointFactory {

    private static final int ONE = 1;

    ApiEndpointFactory() {
        throw new IllegalStateException("Static factory class.");
    }

    public static AbstractApiEndpoint build(final ClientConfig config, final Collection<String> contentIds,
                                            final boolean isSerialized, final boolean fullResponse) {
        if (contentIds == null || contentIds.isEmpty()) {
            throw new IllegalArgumentException("No Content ID provided.");
        }
        AbstractApiEndpoint endpoint;
        if (contentIds.size() == ONE) {
            endpoint = new SingleContentEndpoint(config, contentIds.iterator().next())
                .setSerialized(isSerialized)
                .setFullResponse(fullResponse);
        } else {
            endpoint = new MultipleContentEndpoint(config, contentIds)
                .setSerialized(isSerialized)
                .setFullResponse(fullResponse);
        }
        return endpoint;
    }
}
