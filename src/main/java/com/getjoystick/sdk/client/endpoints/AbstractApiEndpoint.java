package com.getjoystick.sdk.client.endpoints;

import com.fasterxml.jackson.databind.JsonNode;
import com.getjoystick.sdk.client.ClientConfig;
import com.getjoystick.sdk.errors.ApiBadRequestException;
import com.getjoystick.sdk.errors.ApiServerException;
import com.getjoystick.sdk.errors.ApiUnknownException;
import com.getjoystick.sdk.models.RequestBody;
import com.getjoystick.sdk.util.JoystickMapper;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.io.entity.HttpEntities;
import org.apache.hc.core5.http.message.StatusLine;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;

public abstract class AbstractApiEndpoint {

    protected static final String PARAM_RESP_TYPE = "responseType";
    protected static final String NODE_DATA = "data";

    protected ClientConfig config;

    /**
     * Provides REST API endpoint URL.
     *
     * @return REST API endpoint of the exact implementation.
     */
    public abstract String getUrl();

    public abstract String getContentHash(ClientConfig config);

    /**
     * Provide query parameters for request
     *
     * @return array of available query parameters.
     */
    public abstract NameValuePair[] getQueryParameters();

    public abstract JsonNode formatJsonResponse(JsonNode response);

    public JsonNode parseResponseToJson(final ClassicHttpResponse response) throws IOException {
        try (HttpEntity responseEntity = response.getEntity()) {
            if (responseEntity == null) {
                throw new ApiUnknownException("Response body is empty");
            }
            try (InputStream inputStream = responseEntity.getContent()) {
                return JoystickMapper.readTree(inputStream);
            } catch (Exception exception) {
                throw new ApiUnknownException("Response is not in JSON format", exception);
            }
        }
    }


    /**
     * Provides {@link RequestBody} instance to make a call on REST API endpoint.
     *
     * @param userId     - (Required) the unique identifier for a particular user. We use this identifier to split users
     *                   into sticky AB Test groups. Default is empty.
     * @param parameters - (Required) a dictionary of key:value attributes that can be used by Joystick for segmentation
     *                   (different config content can be returned based on the attributes you send).
     *                   Default is empty map.
     * @param semVer     - (Optional) this is the semantic version of the application that is making the request.
     * @return request body.
     */
    public RequestBody getRequestBody(final String userId, final Map<Object, Object> parameters, final String semVer) {
        final RequestBody.RequestBodyBuilder bodyBuilder = RequestBody.builder();
        bodyBuilder.setUserId(userId != null ? userId : "");
        bodyBuilder.setParameters(parameters != null ? parameters : Collections.emptyMap());
        if (semVer != null && !semVer.isEmpty()) {
            bodyBuilder.setSemanticVersion(semVer);
        }
        return bodyBuilder.build();
    }

    public HttpEntity prepareRequestEntity() {
        return HttpEntities.create(outputStream -> {
            JoystickMapper.writeValue(outputStream,
                getRequestBody(
                    config.getUserId(),
                    config.getParams(),
                    config.getSemVer()));
            outputStream.flush();
        }, ContentType.APPLICATION_JSON);
    }

    public JsonNode processResponse(final ClassicHttpResponse response) throws IOException {
        processCommonResponseErrors(response);
        final JsonNode jsonResponse =  parseResponseToJson(response);
        return formatJsonResponse(jsonResponse);
    }

    public void processCommonResponseErrors (final ClassicHttpResponse response) {
        if (response.getCode() >= HttpStatus.SC_SERVER_ERROR) {
            throw new ApiServerException(new StatusLine(response).toString());
        }
        if (response.getCode() >= HttpStatus.SC_BAD_REQUEST && response.getCode() < HttpStatus.SC_SERVER_ERROR) {
            throw new ApiBadRequestException(new StatusLine(response).toString());
        }
        if (response.getCode() != HttpStatus.SC_OK) {
            throw new ApiUnknownException(new StatusLine(response).toString());
        }
    }

}
