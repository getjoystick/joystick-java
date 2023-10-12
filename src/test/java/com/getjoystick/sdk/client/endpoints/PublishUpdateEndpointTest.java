package com.getjoystick.sdk.client.endpoints;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.getjoystick.sdk.errors.ApiBadRequestException;
import com.getjoystick.sdk.errors.ConfigurationException;
import com.getjoystick.sdk.models.PublishData;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.CharStreams;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.io.entity.HttpEntities;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

class PublishUpdateEndpointTest {

    @Test
    void processResponse_noPermissions_error403() {
        String contentId = "id1";
        PublishData data = PublishData.builder()
            .setDescription("test 1")
            .setContent(ImmutableMap.of("k1", "v1", "k2", "v2"))
            .build();
        AbstractApiEndpoint endpoint = new PublishUpdateEndpoint(data, contentId);

        String mockedResponseBody = "{\"data\":false,\"status\":4,\"message\":\"Forbidden. Incorrect permissions.\"}";
        ClassicHttpResponse mockResponse = mock(ClassicHttpResponse.class);
        HttpEntity mockEntity = HttpEntities.create(outputStream -> {
            new ObjectMapper().writeValue(outputStream, mockedResponseBody);
            outputStream.flush();
        }, ContentType.APPLICATION_JSON);
        doReturn(HttpStatus.SC_FORBIDDEN).when(mockResponse).getCode();
        doReturn(mockEntity).when(mockResponse).getEntity();

        final ApiBadRequestException error = assertThrows(ApiBadRequestException.class,
            () -> endpoint.processResponse(mockResponse));
        assertEquals("HTTP/1.1 403 ", error.getMessage());
    }

    @Test
    void processResponse_updateSuccess_code200() throws IOException {
        String contentId = "id1";
        PublishData data = PublishData.builder()
            .setDescription("test 1")
            .setContent(ImmutableMap.of("k1", "v1", "k2", "v2"))
            .build();
        AbstractApiEndpoint endpoint = new PublishUpdateEndpoint(data, contentId);

        String mockedResponseBody = "{\"data\":false,\"status\":1,\"message\":\"Success\"}";
        ClassicHttpResponse mockResponse = mock(ClassicHttpResponse.class);
        HttpEntity mockEntity = HttpEntities.create(outputStream -> {
            new ObjectMapper().writeValue(outputStream, mockedResponseBody);
            outputStream.flush();
        }, ContentType.APPLICATION_JSON);
        doReturn(HttpStatus.SC_OK).when(mockResponse).getCode();
        doReturn(mockEntity).when(mockResponse).getEntity();

        JsonNode jsonNode = endpoint.processResponse(mockResponse);
        assertNotNull(jsonNode);
    }

    @Test
    void prepareRequestEntity_descriptionAndContent_requestWithDataInJson() throws IOException {
        String contentId = "id1";
        PublishData data = PublishData.builder()
            .setDescription("test 1")
            .setContent(ImmutableMap.of("k1", "v1", "k2", "v2"))
            .build();
        AbstractApiEndpoint endpoint = new PublishUpdateEndpoint(data, contentId);
        HttpEntity entity = endpoint.prepareRequestEntity();
        assertNotNull(entity);
        assertEquals(ContentType.APPLICATION_JSON.toString(), entity.getContentType());
        String dataJson;
        try (Reader reader = new InputStreamReader(entity.getContent())) {
            dataJson = CharStreams.toString(reader);
        }
        assertEquals(
            "{\"c\":{\"k1\":\"v1\",\"k2\":\"v2\"},\"d\":\"test 1\",\"m\":[]}",
            dataJson
        );
    }

    @Test
    void getQueryParameters_emptyArray() {
        String contentId = "id1";
        PublishData data = PublishData.builder()
            .setDescription("test 1")
            .setContent(ImmutableMap.of())
            .build();
        AbstractApiEndpoint endpoint = new PublishUpdateEndpoint(data, contentId);
        assertNotNull(endpoint.getQueryParameters());
        assertEquals(0, endpoint.getQueryParameters().length);
    }

    @Test
    void getUrl_validContentId_urlIsCorrect() {
        String contentId = "id1";
        PublishData data = PublishData.builder()
            .setDescription("test 1")
            .setContent(ImmutableMap.of())
            .build();
        AbstractApiEndpoint endpoint = new PublishUpdateEndpoint(data, contentId);
        assertEquals("https://capi.getjoystick.com/api/v1/config/id1", endpoint.getUrl());
    }

    @Test
    void build_nullData_exceptionIsThrown() {
        final ConfigurationException error = assertThrows(ConfigurationException.class,
            () -> new PublishUpdateEndpoint(null, "id1"));
        assertEquals("Data is not provided.", error.getMessage());
    }

    @Test
    void build_nullContentId_exceptionIsThrown() {
        final ConfigurationException error = assertThrows(ConfigurationException.class,
            () -> new PublishUpdateEndpoint(null, null));
        assertEquals("Content ID is not provided.", error.getMessage());
    }

    @Test
    void build_emptyContentId_exceptionIsThrown() {
        final ConfigurationException error = assertThrows(ConfigurationException.class,
            () -> new PublishUpdateEndpoint(null,""));
        assertEquals("Content ID is not provided.", error.getMessage());
    }

}
