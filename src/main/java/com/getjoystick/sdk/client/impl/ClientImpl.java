package com.getjoystick.sdk.client.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.getjoystick.sdk.cache.ApiCache;
import com.getjoystick.sdk.client.Client;
import com.getjoystick.sdk.client.ClientConfig;
import com.getjoystick.sdk.client.endpoints.AbstractApiEndpoint;
import com.getjoystick.sdk.client.endpoints.ApiEndpointFactory;
import com.getjoystick.sdk.errors.ApiUnknownException;
import com.getjoystick.sdk.models.ResponseType;
import com.getjoystick.sdk.util.ApiCacheKeyUtil;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.io.support.ClassicRequestBuilder;
import org.apache.hc.core5.http.message.BasicHeader;
import org.apache.hc.core5.io.CloseMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * Check migration guide <a href="https://hc.apache.org/httpcomponents-client-5.2.x/migration-guide/index.html">...</a>
 * Here implemented classic client <a href="https://hc.apache.org/httpcomponents-client-5.2.x/migration-guide/migration-to-classic.html">...</a>
 */
public class ClientImpl implements Client {

    private static final String API_KEY_HEADER = "x-api-key";

    private final CloseableHttpClient client;

    private final ClientConfig config;
    private final ApiCache<String, String> cache;

    public ClientImpl(final ClientConfig config) {
        this.config = config;
        final Collection<Header> defaultHeaders = new ArrayList<>();
        defaultHeaders.add(new BasicHeader(API_KEY_HEADER, config.getApiKey()));
        defaultHeaders.add(new BasicHeader("Content-Type", "application/json"));
        final RequestConfig requestConfig = RequestConfig.custom()
            .setConnectionRequestTimeout(5000, TimeUnit.MILLISECONDS)
            .setResponseTimeout(5000, TimeUnit.MILLISECONDS)
            .setDefaultKeepAlive(30_000, TimeUnit.MILLISECONDS)
            .build();
        client = HttpClientBuilder.create()
            .setDefaultRequestConfig(requestConfig)
            .setDefaultHeaders(defaultHeaders)
            .build();
        cache = config.getCache();
    }

    /**
     * @param contentIds
     * @return
     */
    @Override
    public String getContents(final Collection<String> contentIds) {
        return getContents(contentIds, null);
    }


    @Override
    public String getContents(final Collection<String> contentIds, final ResponseType responseType) {
        return getContents(contentIds, responseType, false);
    }

    @Override
    public String getContents(final Collection<String> contentIds, final ResponseType responseType,
                              final boolean fullResponse) {
        return getContents(contentIds, responseType, fullResponse, false);
    }

    /**
     * @return response content
     */
    @Override
    public String getContents(final Collection<String> contentIds, final ResponseType responseType,
                              final boolean fullResponse, final boolean refresh) {
        final boolean isSerialized = responseType == ResponseType.SERIALIZED || config.isSerialized();
        final String hash = ApiCacheKeyUtil.getHash(config, contentIds, isSerialized, fullResponse);
        final String cachedContents = refresh ? null: cache.get(hash);
        if (cachedContents != null) {
            return cachedContents;
        }

        final AbstractApiEndpoint contentEndpoint = ApiEndpointFactory.build(contentIds, isSerialized);
        final ClassicHttpRequest httpPost = ClassicRequestBuilder.post(contentEndpoint.getUrl())
            .setEntity(contentEndpoint.prepareRequestEntity(config))
            .addParameters(contentEndpoint.getQueryParameters())
            .build();

        JsonNode responseData;
        try {
            responseData = client.execute(httpPost,
                response -> contentEndpoint.processResponse(response, fullResponse));
        } catch (IOException e) {
            throw new ApiUnknownException("Unable to complete the request", e);
        }
        final String contents = responseData != null ? responseData.toString() : null;
        if (contents != null) {
            cache.put(hash, contents);
        }
        return contents;
    }

    /**
     * Immediately close the client.
     */
    @Override
    public void close() {
        if (client != null) {
            client.close(CloseMode.IMMEDIATE);
        }
    }

}
