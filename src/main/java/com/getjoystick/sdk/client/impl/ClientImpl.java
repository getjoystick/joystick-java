package com.getjoystick.sdk.client.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.getjoystick.sdk.cache.ApiCache;
import com.getjoystick.sdk.client.Client;
import com.getjoystick.sdk.client.ClientConfig;
import com.getjoystick.sdk.client.endpoints.AbstractApiEndpoint;
import com.getjoystick.sdk.client.endpoints.ApiEndpointFactory;
import com.getjoystick.sdk.client.endpoints.PublishUpdateEndpoint;
import com.getjoystick.sdk.client.endpoints.MultipleContentEndpoint;
import com.getjoystick.sdk.client.endpoints.SingleContentEndpoint;
import com.getjoystick.sdk.errors.ApiUnknownException;
import com.getjoystick.sdk.models.JoystickContentOptions;
import com.getjoystick.sdk.models.JoystickFullContent;
import com.getjoystick.sdk.models.PublishData;
import com.getjoystick.sdk.models.ResponseType;
import com.getjoystick.sdk.util.JoystickUtil;
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
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.getjoystick.sdk.util.JoystickUtil.removeTrailingQuotes;

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
     * Get content from Joystick API by contentId
     *
     * @param contentId content id in string format
     * @return configuration content from Joystick
     * @throws ApiUnknownException if the response body does not contain valid JSON or
     *                             any unexpected {@link IOException} is thrown.
     */
    @Override
    public JsonNode getContent(final String contentId) {
        return getContent(contentId, new JoystickContentOptions(false));
    }

    /**
     * Get serialized content from Joystick API by contentId.
     *
     * @param contentId content id in String format
     * @return Joystick configuration serialized as String
     */
    @Override
    public String getContentSerialized(final String contentId) {
        return getContentSerialized(contentId, new JoystickContentOptions(false));
    }

    /**
     * Get full content, including meta and hash, from Joystick API by contentId.
     *
     * @param contentId content id in String format
     * @return Object representing full Joystick content
     */
    @Override
    public JoystickFullContent<JsonNode> getFullContent(final String contentId) {
        return getFullContent(contentId, new JoystickContentOptions(false));
    }

    /**
     * Get full content, including meta and hash, from Joystick API by contentId. Config data is serialized.
     *
     * @param contentId content id in String format
     * @return bject representing full Joystick content, where config data is serialized to string
     */
    @Override
    public JoystickFullContent<String> getFullContentSerialized(final String contentId) {
        return getFullContentSerialized(contentId, new JoystickContentOptions(false));
    }

    /**
     * Get map of configurations for multiple content ids
     *
     * @param contentIds collection of content ids
     * @return map of configuration data by content id
     */
    @Override
    public Map<String, JsonNode> getContents(final Collection<String> contentIds) {
        return getContents(contentIds, new JoystickContentOptions(false));
    }


    /**
     * Get map of serialized configurations for multiple content ids
     *
     * @param contentIds collection of content ids
     * @return map of serialized to string configuration data by content id
     */
    @Override
    public Map<String, String> getContentsSerialized(final Collection<String> contentIds) {
        return getContentsSerialized(contentIds, new JoystickContentOptions(false));
    }

    /**
     * Get map of configurations in full format, including meta and hash, for multiple content ids
     *
     * @param contentIds collection of content ids
     * @return map of full configuration data by content id
     */
    @Override
    public Map<String, JoystickFullContent<JsonNode>> getFullContents(final Collection<String> contentIds) {
        return getFullContents(contentIds, new JoystickContentOptions(false));
    }

    /**
     * Get map of configurations for multiple content ids
     *
     * @param contentIds collection of content ids
     * @return map of full content where data config is serialized to string
     */
    @Override
    public Map<String, JoystickFullContent<String>> getFullContentsSerialized(final Collection<String> contentIds) {
        return getFullContentsSerialized(contentIds, new JoystickContentOptions(false));
    }

    /**
     * Get content from Joystick API by contentId.
     *
     * @param contentId content id in string format
     * @param contentOptions optional parameters for getting Joystick content
     * @return configuration content from Joystick
     * @throws ApiUnknownException if the response body does not contain valid JSON or
     *                             any unexpected {@link IOException} is thrown.
     */
    @Override
    public JsonNode getContent(final String contentId, final JoystickContentOptions contentOptions) {
        final AbstractApiEndpoint singleEndpoint = new SingleContentEndpoint(config, contentId);
        final String singleContent =  getContentsAsString(singleEndpoint, contentOptions);
        return JoystickUtil.readTree(singleContent);
    }

    /**
     * Get serialized content from Joystick API by contentId.
     *
     * @param contentId content id in String format
     * @param contentOptions optional parameters for getting Joystick content
     * @return Joystick configuration serialized as String
     */
    @Override
    public String getContentSerialized(final String contentId, final JoystickContentOptions contentOptions) {
        final AbstractApiEndpoint singleEndpoint = new SingleContentEndpoint(config, contentId).setSerialized(true);
        final String serializedString = getContentsAsString(singleEndpoint, contentOptions);
        return removeTrailingQuotes(serializedString);
    }

    /**
     * Get full content, including meta and hash, from Joystick API by contentId.
     *
     * @param contentId content id in String format
     * @param contentOptions optional parameters for getting Joystick content
     * @return Object representing full Joystick content
     */
    @Override
    public JoystickFullContent<JsonNode> getFullContent(final String contentId,
                                                     final JoystickContentOptions contentOptions) {
        final AbstractApiEndpoint singleEndpoint = new SingleContentEndpoint(config, contentId)
            .setFullResponse(true);
        final String content =  getContentsAsString(singleEndpoint, contentOptions);
        return new JoystickFullContent<>(content, false);
    }

    /**
     * Get full content, including meta and hash, from Joystick API by contentId. Config data is serialized.
     *
     * @param contentId content id in String format
     * @param contentOptions optional parameters for getting Joystick content
     * @return bject representing full Joystick content, where config data is serialized to string
     */
    @Override
    public JoystickFullContent<String> getFullContentSerialized(final String contentId,
                                                                final JoystickContentOptions contentOptions) {
        final AbstractApiEndpoint singleEndpoint = new SingleContentEndpoint(config, contentId)
            .setSerialized(true)
            .setFullResponse(true);
        final String content =  getContentsAsString(singleEndpoint,  contentOptions);
        return new JoystickFullContent<>(content, true);
    }

    /**
     * Get map of configurations for multiple content ids
     *
     * @param contentIds collection of content ids
     * @param contentOptions optional parameters for getting Joystick content
     * @return map of configuration data by content id
     */
    @Override
    public Map<String, JsonNode> getContents(final Collection<String> contentIds,
                                                    final JoystickContentOptions contentOptions) {
        final AbstractApiEndpoint multiEndpoint = new MultipleContentEndpoint(config, contentIds);
        final String content =  getContentsAsString(multiEndpoint, contentOptions);
        final JsonNode jsonNode = JoystickUtil.readTree(content);
        final Map<String, JsonNode> contentMap = new HashMap<>();
        jsonNode.fields().forEachRemaining(nodeEntry ->
            contentMap.put(nodeEntry.getKey(), nodeEntry.getValue())
        );
        return contentMap;
    }

    /**
     * Get map of serialized configurations for multiple content ids
     *
     * @param contentIds collection of content id
     * @param contentOptions optional parameters for getting Joystick content
     * @return map of serialized to string configuration data by content id
     */
    @Override
    public Map<String, String> getContentsSerialized(final Collection<String> contentIds,
                                                     final JoystickContentOptions contentOptions) {
        final AbstractApiEndpoint multiEndpoint = new MultipleContentEndpoint(config, contentIds)
            .setSerialized(true);
        final String content =  getContentsAsString(multiEndpoint, contentOptions);
        final JsonNode jsonNode = JoystickUtil.readTree(content);
        final Map<String, String> contentMap = new HashMap<>();
        jsonNode.fields().forEachRemaining(nodeEntry -> {
            final JsonNode nodeValue = nodeEntry.getValue();
            final String serializedContent = nodeValue != null ? nodeValue.toString() : null;
            contentMap.put(nodeEntry.getKey(), removeTrailingQuotes(serializedContent));
        });
        return contentMap;
    }

    /**
     * Get map of configurations in full format, including meta and hash, for multiple content ids
     *
     * @param contentIds collection of content ids
     * @param contentOptions optional parameters for getting Joystick content
     * @return map of full configuration data by content id
     */
    @Override
    public Map<String, JoystickFullContent<JsonNode>> getFullContents(final Collection<String> contentIds,
                                                                final JoystickContentOptions contentOptions) {
        final AbstractApiEndpoint multiEndpoint = new MultipleContentEndpoint(config, contentIds)
            .setFullResponse(true);
        final String content =  getContentsAsString(multiEndpoint, contentOptions);
        final JsonNode jsonNode = JoystickUtil.readTree(content);
        final Map<String, JoystickFullContent<JsonNode>> contentMap = new HashMap<>();
        jsonNode.fields().forEachRemaining(nodeEntry -> {
            final JsonNode jsonContent = nodeEntry.getValue();
            contentMap.put(nodeEntry.getKey(),
                new JoystickFullContent<>(jsonContent, false));
        });
        return contentMap;
    }

    /**
     * Get map of configurations for multiple content ids
     *
     * @param contentIds collection of content ids
     * @param contentOptions optional parameters for getting Joystick content
     * @return map of full content where data config is serialized to string
     */
    @Override
    public Map<String, JoystickFullContent<String>> getFullContentsSerialized(final Collection<String> contentIds,
                                                                              final JoystickContentOptions contentOptions) {
        final AbstractApiEndpoint multiEndpoint = new MultipleContentEndpoint(config, contentIds)
            .setSerialized(true)
            .setFullResponse(true);
        final String content =  getContentsAsString(multiEndpoint, contentOptions);
        final JsonNode jsonNode = JoystickUtil.readTree(content);
        final Map<String, JoystickFullContent<String>> contentMap = new HashMap<>();
        jsonNode.fields().forEachRemaining(nodeEntry -> {
            final JsonNode jsonContent = nodeEntry.getValue();
            if(jsonContent == null) {
                contentMap.put(nodeEntry.getKey(), null);
            } else {
                contentMap.put(nodeEntry.getKey(), new JoystickFullContent<>(jsonContent, true));
            }
        });
        return contentMap;
    }


    /**
     * Get Joystick content in String format
     *
     * @param contentIds collection of content ids
     * @return Joystick content in String format
     */
    @Override
    public String getContentsAsString(final Collection<String> contentIds) {
        return getContentsAsString(contentIds, null);
    }

    /**
     * Get Joystick content in String format
     *
     * @param contentIds collection of content ids in string format
     * @param responseType declares if response should be serialized or not serialized
     * @return Joystick content in String format
     */
    @Override
    public String getContentsAsString(final Collection<String> contentIds, final ResponseType responseType) {
        return getContentsAsString(contentIds, responseType, false);
    }

    /**
     * Get Joystick content in String format
     *
     * @param contentIds collection of content ids in string format
     * @param responseType declares if response should be serialized or not serialized
     * @param fullResponse if response should be in full format
     * @return Joystick content in String format
     */
    @Override
    public String getContentsAsString(final Collection<String> contentIds, final ResponseType responseType,
                                      final boolean fullResponse) {
        return getContentsAsString(contentIds, responseType, fullResponse, false);
    }

    /**
     * Get Joystick content in String format
     *
     * @param contentIds collection of content ids in string format
     * @param responseType declares if response should be serialized or not serialized
     * @param fullResponse if response should be in full format
     * @param refresh if true direct call to Joystick will be made skipping cache
     * @return Joystick content in String format
     */
    @Override
    public String getContentsAsString(final Collection<String> contentIds, final ResponseType responseType,
                                      final boolean fullResponse, final boolean refresh) {
        final boolean isSerialized = responseType == ResponseType.SERIALIZED;
        final AbstractApiEndpoint contentEndpoint = ApiEndpointFactory.build(config, contentIds, isSerialized, fullResponse);
        return getContentsAsString(contentEndpoint, new JoystickContentOptions(refresh));
    }

    /**
     * Get Joystick content in String format
     *
     * @param contentEndpoint endpoint to load Joystick content
     * @param contentOptions optional parameters for getting Joystick content
     * @return Joystick content in String format
     */
    private String getContentsAsString(final AbstractApiEndpoint contentEndpoint,
                                       final JoystickContentOptions contentOptions) {
        final String hash = contentEndpoint.getContentHash(config);
        final String cachedContents = contentOptions.isRefresh() ? null: cache.get(hash);
        if (cachedContents != null) {
            return cachedContents;
        }

        final ClassicHttpRequest httpPost = ClassicRequestBuilder.post(contentEndpoint.getUrl())
            .setEntity(contentEndpoint.prepareRequestEntity())
            .addParameters(contentEndpoint.getQueryParameters())
            .build();

        JsonNode responseData;
        try {
            responseData = client.execute(httpPost, contentEndpoint::processResponse);
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
     * Publish update for already existing content in Joystick
     *
     * @param contentId id of existing content
     * @param data includes data to be updated in Joystick
     */
    @Override
    public void publishContentUpdate(final String contentId, final PublishData data) {
        if (contentId == null || contentId.trim().isEmpty()) {
            throw new IllegalArgumentException("No Content ID provided.");
        }
        final AbstractApiEndpoint endpoint = new PublishUpdateEndpoint(data, contentId);
        final ClassicHttpRequest httpPut = ClassicRequestBuilder.put(endpoint.getUrl())
            .setEntity(endpoint.prepareRequestEntity())
            .addParameters(endpoint.getQueryParameters())
            .build();
        try {
            client.execute(httpPut, endpoint::processResponse);
        } catch (IOException e) {
            throw new ApiUnknownException("Unable to complete the request", e);
        }
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
