package com.getjoystick.sdk.client.endpoints;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.getjoystick.sdk.errors.ConfigurationException;
import com.getjoystick.sdk.errors.MultipleContentsApiException;
import com.getjoystick.sdk.models.RequestBody;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MultipleContentEndpointTest {

    private static final ObjectMapper OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = JsonMapper.builder()
            .enable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY)
            .serializationInclusion(JsonInclude.Include.NON_NULL)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .build();
    }

    @Test
    void getQueryParameters_allParamsSet_paramOrderAndValuesCorrect() {
        Set<String> contentIds = ImmutableSet.of("id1", "id2","id3");
        AbstractApiEndpoint multipleConfigsApiEndpoint = MultipleContentEndpoint.builder()
            .setContentIds(contentIds)
            .setSerialized(true)
            .build();
        NameValuePair[] queryParams = multipleConfigsApiEndpoint.getQueryParameters();
        assertEquals(new BasicNameValuePair("dynamic", "true"), queryParams[0]);
        assertEquals(new BasicNameValuePair("responseType", "serialized"), queryParams[1]);
        assertEquals(new BasicNameValuePair("c", "[\"id1\",\"id2\",\"id3\"]"), queryParams[2]);
        assertEquals(3, queryParams.length);
    }

    @Test
    void getRequestBody_allParamsSet_validRequestBody() {
        Set<String> contentIds = ImmutableSet.of("id1", "id2","id3");
        AbstractApiEndpoint multipleConfigsApiEndpoint = MultipleContentEndpoint.builder()
            .setContentIds(contentIds)
            .setSerialized(true)
            .build();
        RequestBody requestBody = multipleConfigsApiEndpoint
            .getRequestBody("userId", ImmutableMap.of("p_key1", "p_value1"), "1.1");
        assertEquals("userId", requestBody.getU());
        assertEquals(ImmutableMap.of("p_key1", "p_value1"), requestBody.getP());
        assertEquals("1.1", requestBody.getV());
    }

    @Test
    void getRequestBody_nullParams_requestBodyWithDefaultParams() {
        Set<String> contentIds = ImmutableSet.of("id1", "id2","id3");
        AbstractApiEndpoint multipleConfigsApiEndpoint = MultipleContentEndpoint.builder()
            .setContentIds(contentIds)
            .setSerialized(true)
            .build();
        RequestBody expectedResult = RequestBody.builder().build();
        RequestBody requestBody = multipleConfigsApiEndpoint
            .getRequestBody(null, null, null);
        assertEquals("", requestBody.getU());
        assertEquals(ImmutableMap.of(), requestBody.getP());
        assertNull(requestBody.getV());
        assertEquals(expectedResult, requestBody);
    }

    @Test
    void build_nullContentIds_exceptionIsThrown() {
        final ConfigurationException error = assertThrows(ConfigurationException.class,
            () -> MultipleContentEndpoint.builder().build());
        assertEquals( "Content IDs are not provided.", error.getMessage());
    }

    @Test
    void build_emptyContentIds_exceptionIsThrown() {
        final ConfigurationException error = assertThrows(ConfigurationException.class,
            () -> MultipleContentEndpoint.builder().setContentIds(ImmutableSet.of()).build());
        assertEquals( "Content IDs are not provided.", error.getMessage());
    }

    @Test
    void formatJsonResponse_fullResponseWithoutErrors_fullResponseReturned() throws JsonProcessingException {
        final JsonNode jsonNode = OBJECT_MAPPER.readTree("{\"dev_test_001\":{\"data\":{\"config_name\":\"initial-test-config-dev-001\"}," +
            "\"hash\":\"2f5aa20f\",\"meta\":{\"uid\":0,\"mod\":0,\"variants\":[],\"seg\":[]}}," +
            "\"dev_test_002\":{\"data\":{\"config_name\":\"initial-test-config-dev-002\"}," +
            "\"hash\":\"2f5aa20f\",\"meta\":{\"uid\":0,\"mod\":0,\"variants\":[],\"seg\":[]}}}");

        MultipleContentEndpoint multipleContent = MultipleContentEndpoint.builder()
            .setContentIds(ImmutableSet.of("id1", "id2"))
            .setFullResponse(true)
            .build();

        final JsonNode result = multipleContent.formatJsonResponse(jsonNode);
        assertEquals(jsonNode, result);
    }

    @Test
    void formatJsonResponse_fullResponseWithErrors_multipleContentsApiExceptionThrown() throws JsonProcessingException {
        final JsonNode jsonNode = OBJECT_MAPPER.readTree("{\"dev_test_001\":{\"data\":{\"config_name\":\"initial-test-config-dev-001\"}," +
            "\"hash\":\"2f5aa20f\",\"meta\":{\"uid\":0,\"mod\":0,\"variants\":[],\"seg\":[]}}," +
            "\"dev_test_002\":\"Some error occurred\"}");

        MultipleContentEndpoint multipleContent = MultipleContentEndpoint.builder()
            .setContentIds(ImmutableSet.of("id1", "id2"))
            .setFullResponse(true)
            .build();

        final JsonNode resultErrorNode = OBJECT_MAPPER.readTree("{\"dev_test_002\":\"Some error occurred\"}");
        final MultipleContentsApiException error = assertThrows(MultipleContentsApiException.class,
            () -> multipleContent.formatJsonResponse(jsonNode));
        assertEquals( "Response from remote server contains errors:"
            + System.lineSeparator() + resultErrorNode.toPrettyString(), error.getMessage());
    }

    @Test
    void formatJsonResponse_shortResponseWithoutErrors_shortResponseReturned() throws JsonProcessingException {
        final JsonNode jsonNode = OBJECT_MAPPER.readTree("{\"dev_test_001\":{\"data\":{\"config_name\":\"initial-test-config-dev-001\"}," +
            "\"hash\":\"2f5aa20f\",\"meta\":{\"uid\":0,\"mod\":0,\"variants\":[],\"seg\":[]}}," +
            "\"dev_test_002\":{\"data\":{\"config_name\":\"initial-test-config-dev-002\"}," +
            "\"hash\":\"2f5aa20f\",\"meta\":{\"uid\":0,\"mod\":0,\"variants\":[],\"seg\":[]}}}");

        MultipleContentEndpoint multipleContent = MultipleContentEndpoint.builder()
            .setContentIds(ImmutableSet.of("id1", "id2"))
            .setFullResponse(false)
            .build();

        final JsonNode result = multipleContent.formatJsonResponse(jsonNode);
        assertEquals("{\"dev_test_001\":{\"config_name\":\"initial-test-config-dev-001\"}," +
            "\"dev_test_002\":{\"config_name\":\"initial-test-config-dev-002\"}}", result.toString());
    }

    @Test
    void formatJsonResponse_shortResponseWithErrors_multipleContentsApiExceptionThrown() throws JsonProcessingException {
        final JsonNode jsonNode = OBJECT_MAPPER.readTree("{\"dev_test_001\":{\"data\":{\"config_name\":\"initial-test-config-dev-001\"}," +
            "\"hash\":\"2f5aa20f\",\"meta\":{\"uid\":0,\"mod\":0,\"variants\":[],\"seg\":[]}}," +
            "\"dev_test_002\":\"Some error occurred\"}");

        MultipleContentEndpoint multipleContent = MultipleContentEndpoint.builder()
            .setContentIds(ImmutableSet.of("id1", "id2"))
            .build();

        final JsonNode resultErrorNode = OBJECT_MAPPER.readTree("{\"dev_test_002\":\"Some error occurred\"}");
        final MultipleContentsApiException error = assertThrows(MultipleContentsApiException.class,
            () -> multipleContent.formatJsonResponse(jsonNode));
        assertEquals( "Response from remote server contains errors:"
            + System.lineSeparator() + resultErrorNode.toPrettyString(), error.getMessage());
    }

}
