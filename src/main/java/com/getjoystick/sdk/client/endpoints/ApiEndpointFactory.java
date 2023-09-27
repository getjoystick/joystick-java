package com.getjoystick.sdk.client.endpoints;

import java.util.Collection;

public class ApiEndpointFactory {

    private static final int ONE = 1;

    public static AbstractApiEndpoint build(final Collection<String> contentIds) {
        return build(contentIds, false);
    }

    public static AbstractApiEndpoint build(final Collection<String> contentIds,
                                            final boolean isSerialized) {
        if (contentIds == null || contentIds.isEmpty()) {
            throw new IllegalArgumentException("No Content ID provided.");
        }
        AbstractApiEndpoint endpoint;
        if (contentIds.size() == ONE) {
            endpoint = SingleContentEndpoint.builder()
                .setContentId(contentIds.iterator().next())
                .setSerialized(isSerialized)
                .build();
        } else {
            endpoint = MultipleContentEndpoint.builder()
                .setContentIds(contentIds)
                .setSerialized(isSerialized)
                .build();
        }
        return endpoint;
    }

}
