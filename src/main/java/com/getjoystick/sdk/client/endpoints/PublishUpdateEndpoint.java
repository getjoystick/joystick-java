package com.getjoystick.sdk.client.endpoints;

import com.fasterxml.jackson.databind.JsonNode;
import com.getjoystick.sdk.client.ClientConfig;
import com.getjoystick.sdk.errors.ConfigurationException;
import com.getjoystick.sdk.models.PublishData;
import com.getjoystick.sdk.util.JoystickMapper;
import lombok.NonNull;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.io.entity.HttpEntities;

import java.io.IOException;

public class PublishUpdateEndpoint extends AbstractApiEndpoint {

    private static final String API_URL = "https://capi.getjoystick.com/api/v1/config/%s";

    private final String contentId;
    private final PublishData data;

    public PublishUpdateEndpoint(final PublishData data, final String contentId) {
        if (contentId == null || contentId.trim().isEmpty()) {
            throw new ConfigurationException("Content ID is not provided.");
        }
        if (data == null) {
            throw new ConfigurationException("Data is not provided.");
        }
        this.data = data;
        this.contentId = contentId;
    }

    @Override
    public String getUrl() {
        return String.format(API_URL, contentId);
    }

    @Override
    public String getContentHash(ClientConfig config) {
        throw new UnsupportedOperationException("Method is not implemented");
    }

    @Override
    public @NonNull NameValuePair[] getQueryParameters() {
        return new NameValuePair[0];
    }

    /**
     * Unsupported operation for publish content
     *
     * @param jsonNode json content
     * @return throws UnsupportedOperationException
     */
    @Override
    public JsonNode formatJsonResponse(final JsonNode jsonNode) {
        throw new UnsupportedOperationException("method is not implemented");
    }

    @Override
    public JsonNode processResponse(ClassicHttpResponse response) throws IOException {
        processCommonResponseErrors(response);
        return parseResponseToJson(response);
    }

    @Override
    public HttpEntity prepareRequestEntity() {
        return HttpEntities.create(outputStream -> {
            JoystickMapper.writeValue(outputStream, data);
            outputStream.flush();
        }, ContentType.APPLICATION_JSON);
    }
}
