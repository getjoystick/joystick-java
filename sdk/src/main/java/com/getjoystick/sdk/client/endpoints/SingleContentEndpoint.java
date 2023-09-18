package com.getjoystick.sdk.client.endpoints;

import com.fasterxml.jackson.databind.JsonNode;
import com.getjoystick.sdk.errors.ConfigurationException;
import lombok.Builder;
import lombok.NonNull;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import java.util.ArrayList;
import java.util.Collection;

@Builder(setterPrefix = "set")
public class SingleContentEndpoint extends AbstractApiEndpoint {

    private static final String SINGLE_CONFIG_URL = "https://api.getjoystick.com/api/v1/config/%s/dynamic";

    private String contentId;
    private boolean serialized;

    @Override
    public String getUrl() {
        return String.format(SINGLE_CONFIG_URL, contentId);
    }

    @Override
    public @NonNull NameValuePair[] getQueryParameters() {
        final Collection<NameValuePair> qParams = new ArrayList<>();
        if (serialized) {
            qParams.add(new BasicNameValuePair(PARAM_RESP_TYPE, "serialized"));
        }
        return qParams.toArray(new NameValuePair[0]);
    }

    /**
     * @param jsonNode
     * @param fullResponse
     * @return
     */
    @Override
    public JsonNode formatJsonResponse(final JsonNode jsonNode, final boolean fullResponse) {
        return fullResponse ? jsonNode : jsonNode.findValue(NODE_DATA);
    }

    public static SingleContentEndpoint.SingleContentEndpointBuilder builder() {
        return new OverrideSingleContentEndpointBuilder();
    }

    private static class OverrideSingleContentEndpointBuilder extends
        SingleContentEndpoint.SingleContentEndpointBuilder {

        @Override
        public SingleContentEndpoint build() {
            if (super.contentId == null || super.contentId.trim().isEmpty()) {
                throw new ConfigurationException("Content ID is not provided.");
            }
            return super.build();
        }
    }
}
