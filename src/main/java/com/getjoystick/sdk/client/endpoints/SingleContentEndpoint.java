package com.getjoystick.sdk.client.endpoints;

import com.fasterxml.jackson.databind.JsonNode;
import com.getjoystick.sdk.client.ClientConfig;
import com.getjoystick.sdk.errors.ConfigurationException;
import com.getjoystick.sdk.util.ApiCacheKeyUtil;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Collection;
public class SingleContentEndpoint extends AbstractApiEndpoint {

    private static final String SINGLE_CONFIG_URL = "https://api.getjoystick.com/api/v1/config/%s/dynamic";

    private final String contentId;
    private boolean serialized;
    private boolean fullResponse;

    public SingleContentEndpoint(final ClientConfig config, final String contentId) {
        if (contentId == null || contentId.isEmpty()) {
            throw new ConfigurationException("Content ID is not provided.");
        }
        if(config == null) {
            throw new ConfigurationException("Config is not provided.");
        }
        this.config = config;
        this.contentId = contentId;
    }

    @Override
    public String getUrl() {
        return String.format(SINGLE_CONFIG_URL, contentId);
    }

    private boolean isSerialized() {
        return serialized;
    }

    /**
     * Get content hash in String format
     *
     * @param config client configuration
     * @return content hash in String format
     */
    @Override
    public String getContentHash(final ClientConfig config) {
        return ApiCacheKeyUtil.getHash(config, contentId, isSerialized(), fullResponse);
    }

    /**
     * Get array of query parameters
     *
     * @return array of parameters as NameValuePair
     */
    @Override
    public NameValuePair[] getQueryParameters() {
        final Collection<NameValuePair> qParams = new ArrayList<>();
        if (isSerialized()) {
            qParams.add(new BasicNameValuePair(PARAM_RESP_TYPE, "serialized"));
        }
        return qParams.toArray(new NameValuePair[0]);
    }

    /**
     * Format json to contain either full content or data only
     *
     * @param jsonNode to format
     * @return response formatted either in full or in short mode
     */
    @Override
    public JsonNode formatJsonResponse(final JsonNode jsonNode) {
        return fullResponse ? jsonNode : jsonNode.findValue(NODE_DATA);
    }

    public SingleContentEndpoint setSerialized(final boolean serialized) {
        this.serialized = serialized;
        return this;
    }

    public SingleContentEndpoint setFullResponse(final boolean fullResponse) {
        this.fullResponse = fullResponse;
        return this;
    }
}
