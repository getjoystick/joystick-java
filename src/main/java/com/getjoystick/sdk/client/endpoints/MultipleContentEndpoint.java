package com.getjoystick.sdk.client.endpoints;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.getjoystick.sdk.client.ClientConfig;
import com.getjoystick.sdk.errors.ConfigurationException;
import com.getjoystick.sdk.errors.MultipleContentsApiException;
import com.getjoystick.sdk.util.ApiCacheKeyUtil;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Accessors(chain = true)
public class MultipleContentEndpoint extends AbstractApiEndpoint {

    private static final String PARAM_CONTENT_IDS = "c";
    private static final String PARAM_DYNAMIC = "dynamic";
    private static final String MULTI_CONFIG_URL = "https://api.getjoystick.com/api/v1/combine/";

    private Collection<String> contentIds;
    @Setter
    private boolean serialized;
    @Setter
    private boolean fullResponse;

    public MultipleContentEndpoint(final ClientConfig config, final Collection<String> contentIds) {
        if (contentIds == null || contentIds.isEmpty()) {
            throw new ConfigurationException("Content IDs are not provided.");
        }
        if(config == null) {
            throw new ConfigurationException("Config is not provided.");
        }
        this.config = config;
        this.contentIds = contentIds;
    }

    @Override
    public String getUrl() {
        return MULTI_CONFIG_URL;
    }

    private boolean isSerialized() {
        return serialized || config.isSerialized();
    }

    @Override
    public @NonNull NameValuePair[] getQueryParameters() {
        final Collection<NameValuePair> qParams = new ArrayList<>();
        qParams.add(new BasicNameValuePair(PARAM_DYNAMIC, "true"));
        if (isSerialized()) {
            qParams.add(new BasicNameValuePair(PARAM_RESP_TYPE, "serialized"));
        }
        try {
            qParams.add(new BasicNameValuePair(PARAM_CONTENT_IDS, OBJECT_MAPPER.writeValueAsString(contentIds)));
        } catch (JsonProcessingException e) {
            throw new ConfigurationException(e);
        }
        return qParams.toArray(new NameValuePair[0]);
    }

    /**
     * @return
     */
    @Override
    public String getContentHash(final ClientConfig config) {
        return ApiCacheKeyUtil.getHash(config, contentIds, isSerialized(), fullResponse);
    }

    /**
     * @param jsonNode
     * @return
     */
    @Override
    public JsonNode formatJsonResponse(final JsonNode jsonNode) {
        validateResponse(jsonNode);
        return fullResponse ? jsonNode : extractDataNodesOnly(jsonNode);
    }

    private void validateResponse(final JsonNode jsonNode) {
        final Map<String, JsonNode> errorMap = new HashMap<>();
        jsonNode.fields().forEachRemaining(nodeEntry -> {
            final JsonNode nodeValue = nodeEntry.getValue();
            if(nodeEntry.getKey() != null && nodeValue.getNodeType() == JsonNodeType.STRING) {
                errorMap.put(nodeEntry.getKey(), nodeValue);
            }
        });
        if(!errorMap.isEmpty()) {
            final StringBuilder errorBuffer = new StringBuilder("Response from remote server contains errors:");
            errorBuffer.append(System.lineSeparator());
            errorBuffer.append(new ObjectNode(JsonNodeFactory.instance, errorMap).toPrettyString());
            throw new MultipleContentsApiException(errorBuffer.toString());
        }
    }

    private JsonNode extractDataNodesOnly(final JsonNode jsonNode) {
        final Map<String, JsonNode> dataByContentId = new HashMap<>();
        jsonNode.fields().forEachRemaining(nodeEntry -> {
            final JsonNode nodeValue = nodeEntry.getValue();
            if(nodeEntry.getKey() != null && nodeValue.getNodeType() == JsonNodeType.OBJECT) {
                final JsonNode dataNode = nodeValue.findValue(NODE_DATA);
                dataByContentId.put(nodeEntry.getKey(), dataNode);
            }
        });
        return new ObjectNode(JsonNodeFactory.instance, dataByContentId);
    }
}
