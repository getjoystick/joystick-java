package com.getjoystick.sdk.client.endpoints;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.getjoystick.sdk.client.ClientConfig;
import com.getjoystick.sdk.errors.ApiBadRequestException;
import com.getjoystick.sdk.errors.ApiServerException;
import com.getjoystick.sdk.errors.ApiUnknownException;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.HttpEntities;
import org.apache.hc.core5.http.message.BasicClassicHttpResponse;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AbstractApiEndpointTest {

    private static final String API_KEY = "test-api-key";

    private static final ObjectMapper OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = JsonMapper.builder()
            .enable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY)
            .serializationInclusion(JsonInclude.Include.NON_NULL)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .build();
    }

    @Test
    void prepareRequestEntity_singleContentIdNoParams_ddefaultRequestBody() throws IOException {
        final ClientConfig config = ClientConfig.builder().setApiKey(API_KEY).build();
        final AbstractApiEndpoint apiEndpoint = ApiEndpointFactory.build(ImmutableSet.of("id1"));
        HttpEntity requestEntity = apiEndpoint.prepareRequestEntity(config);
        assertEquals("application/json; charset=UTF-8", requestEntity.getContentType());
        assertEquals("{\"p\":{},\"u\":\"\"}",OBJECT_MAPPER.readTree(requestEntity.getContent()).toString());
    }

    @Test
    void prepareRequestEntity_multipleContentIdsNoParams_defaultRequestBody() throws IOException {
        final ClientConfig config = ClientConfig.builder().setApiKey(API_KEY).build();
        final AbstractApiEndpoint apiEndpoint = ApiEndpointFactory.build(ImmutableSet.of("id1", "id2"));
        HttpEntity requestEntity = apiEndpoint.prepareRequestEntity(config);
        assertEquals("application/json; charset=UTF-8", requestEntity.getContentType());
        assertEquals("{\"p\":{},\"u\":\"\"}",OBJECT_MAPPER.readTree(requestEntity.getContent()).toString());
    }

    @Test
    void prepareRequestEntity_multipleContentIdsWithParams_requestBodyWithParams() throws IOException {
        final ClientConfig config = ClientConfig.builder()
            .setApiKey(API_KEY)
            .setSemVer("0.0.1")
            .setParams(ImmutableMap.of("key1", "value1", "k2", "v2"))
            .setUserId("bestUser")
            .build();
        final AbstractApiEndpoint apiEndpoint = ApiEndpointFactory.build(ImmutableSet.of("id1", "id2"));
        HttpEntity requestEntity = apiEndpoint.prepareRequestEntity(config);
        assertEquals("application/json; charset=UTF-8", requestEntity.getContentType());
        assertEquals("{\"p\":{\"key1\":\"value1\",\"k2\":\"v2\"},\"u\":\"bestUser\",\"v\":\"0.0.1\"}",OBJECT_MAPPER.readTree(requestEntity.getContent()).toString());
    }

    @Test
    void processCommonResponseErrors_500Code_apiServerExceptionThrown() {
        final AbstractApiEndpoint apiEndpoint = ApiEndpointFactory.build(ImmutableSet.of("id1"));
        assertThrows(ApiServerException.class,
            () -> apiEndpoint.processCommonResponseErrors(new BasicClassicHttpResponse(500)));
    }

    @Test
    void processCommonResponseErrors_400Code_apiBadRequestExceptionThrown() {
        final AbstractApiEndpoint apiEndpoint = ApiEndpointFactory.build(ImmutableSet.of("id1"));
        assertThrows(ApiBadRequestException.class,
            () -> apiEndpoint.processCommonResponseErrors(new BasicClassicHttpResponse(400)));
        assertThrows(ApiBadRequestException.class,
            () -> apiEndpoint.processCommonResponseErrors(new BasicClassicHttpResponse(499)));
    }

    @Test
    void processCommonResponseErrors_300Code_apiUnknownExceptionThrown() {
        final AbstractApiEndpoint apiEndpoint = ApiEndpointFactory.build(ImmutableSet.of("id1"));
        assertThrows(ApiUnknownException.class,
            () -> apiEndpoint.processCommonResponseErrors(new BasicClassicHttpResponse(300)));
    }

    @Test
    void processCommonResponseErrors_nullResponseEntity_apiUnknownExceptionThrown() {
        ClassicHttpResponse response = new BasicClassicHttpResponse(200);
        response.setEntity(null);
        final AbstractApiEndpoint apiEndpoint = ApiEndpointFactory.build(ImmutableSet.of("id1"));
        final ApiUnknownException error = assertThrows(ApiUnknownException.class,
            () -> apiEndpoint.parseResponseToJson(response));
        assertEquals( "Response body is empty", error.getMessage());
    }

    @Test
    void processResponse_validJsonResponseInShortFormat_validResult() throws IOException {
        ClassicHttpResponse response = new BasicClassicHttpResponse(200);
        response.setEntity(HttpEntities.create("{\"data\":{\"hello\":\"world\"},\"hash\":\"dbf261eb\",\"meta\":" +
            "{\"uid\":0,\"mod\":0,\"variants\":[],\"seg\":[]}}"));
        final AbstractApiEndpoint apiEndpoint = ApiEndpointFactory.build(ImmutableSet.of("id1"));
        JsonNode jsonNode = apiEndpoint.processResponse(response);
        assertEquals("{\"hello\":\"world\"}", jsonNode.toString());
    }

    @Test
    void processResponse_validSerializedJsonResponseInShortFormat_validResult() throws IOException {
        ClassicHttpResponse response = new BasicClassicHttpResponse(200);
        response.setEntity(HttpEntities.create("{\"data\":\"{\\\"hello\\\":\\\"world\\\"}\",\"hash\":\"dbf261eb\"," +
            "\"meta\":{\"uid\":0,\"mod\":0,\"variants\":[],\"seg\":[]}}"));
        final AbstractApiEndpoint apiEndpoint = ApiEndpointFactory.build(ImmutableSet.of("id1"));
        JsonNode jsonNode = apiEndpoint.processResponse(response);
        assertEquals("\"{\\\"hello\\\":\\\"world\\\"}\"", jsonNode.toString());
    }

    @Test
    void processResponse_validJsonResponseInFullFormat_validResult() throws IOException {
        ClassicHttpResponse response = new BasicClassicHttpResponse(200);
        response.setEntity(HttpEntities.create("{\"data\":{\"hello\":\"world\"},\"hash\":\"dbf261eb\",\"meta\":" +
            "{\"uid\":0,\"mod\":0,\"variants\":[],\"seg\":[]}}"));
        final AbstractApiEndpoint apiEndpoint = ApiEndpointFactory.build(ImmutableSet.of("id1"), false, true);
        JsonNode jsonNode = apiEndpoint.processResponse(response);
        assertEquals("{\"data\":{\"hello\":\"world\"},\"hash\":\"dbf261eb\"," +
            "\"meta\":{\"uid\":0,\"mod\":0,\"variants\":[],\"seg\":[]}}", jsonNode.toString());
    }

    @Test
    void processResponse_validSerializedJsonResponseInFullFormat_validResult() throws IOException {
        ClassicHttpResponse response = new BasicClassicHttpResponse(200);
        response.setEntity(HttpEntities.create("{\"data\":\"{\\\"hello\\\":\\\"world\\\"}\",\"hash\":\"dbf261eb\"," +
            "\"meta\":{\"uid\":0,\"mod\":0,\"variants\":[],\"seg\":[]}}"));
        final AbstractApiEndpoint apiEndpoint = ApiEndpointFactory.build(ImmutableSet.of("id1"),
            false, true);
        JsonNode jsonNode = apiEndpoint.processResponse(response);
        assertEquals("{\"data\":\"{\\\"hello\\\":\\\"world\\\"}\",\"hash\":\"dbf261eb\"," +
            "\"meta\":{\"uid\":0,\"mod\":0,\"variants\":[],\"seg\":[]}}", jsonNode.toString());
    }

    @Test
    void processResponse_invalidJsonResponse_apiUnknownExceptionThrown() {
        ClassicHttpResponse response = new BasicClassicHttpResponse(200);
        response.setEntity(HttpEntities.create("{\"data\":,,,{} some invalid Json}}"));
        final AbstractApiEndpoint apiEndpoint = ApiEndpointFactory.build(ImmutableSet.of("id1"));
        final ApiUnknownException error = assertThrows(ApiUnknownException.class,
            () -> apiEndpoint.processResponse(response));
        assertEquals( "Response is not in JSON format", error.getMessage());
    }

}