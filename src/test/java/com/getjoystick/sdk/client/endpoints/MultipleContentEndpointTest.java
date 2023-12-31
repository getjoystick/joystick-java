package com.getjoystick.sdk.client.endpoints;

import com.fasterxml.jackson.databind.JsonNode;
import com.getjoystick.sdk.client.ClientConfig;
import com.getjoystick.sdk.errors.ConfigurationException;
import com.getjoystick.sdk.errors.MultipleContentsApiException;
import com.getjoystick.sdk.models.RequestBody;
import com.getjoystick.sdk.util.JoystickUtil;
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

    private static final ClientConfig CONFIG = ClientConfig.builder().setApiKey("test-api-key").build();

    @Test
    void getQueryParameters_allParamsSet_paramOrderAndValuesCorrect() {
        Set<String> contentIds = ImmutableSet.of("id1", "id2","id3");
        AbstractApiEndpoint multipleConfigsApiEndpoint = new MultipleContentEndpoint(CONFIG, contentIds)
            .setSerialized(true);
        NameValuePair[] queryParams = multipleConfigsApiEndpoint.getQueryParameters();
        assertEquals(new BasicNameValuePair("dynamic", "true"), queryParams[0]);
        assertEquals(new BasicNameValuePair("responseType", "serialized"), queryParams[1]);
        assertEquals(new BasicNameValuePair("c", "[\"id1\",\"id2\",\"id3\"]"), queryParams[2]);
        assertEquals(3, queryParams.length);
    }

    @Test
    void getRequestBody_allParamsSet_validRequestBody() {
        Set<String> contentIds = ImmutableSet.of("id1", "id2","id3");
        AbstractApiEndpoint multipleConfigsApiEndpoint = new MultipleContentEndpoint(CONFIG, contentIds)
            .setSerialized(true);
        RequestBody requestBody = multipleConfigsApiEndpoint
            .getRequestBody("userId", ImmutableMap.of("p_key1", "p_value1"), "1.1");
        assertEquals("userId", requestBody.getUserId());
        assertEquals(ImmutableMap.of("p_key1", "p_value1"), requestBody.getParameters());
        assertEquals("1.1", requestBody.getSemanticVersion());
    }

    @Test
    void getRequestBody_nullParams_requestBodyWithDefaultParams() {
        Set<String> contentIds = ImmutableSet.of("id1", "id2","id3");
        AbstractApiEndpoint multipleConfigsApiEndpoint = new MultipleContentEndpoint(CONFIG, contentIds)
            .setSerialized(true);
        RequestBody expectedResult = RequestBody.builder().build();
        RequestBody requestBody = multipleConfigsApiEndpoint
            .getRequestBody(null, null, null);
        assertEquals("", requestBody.getUserId());
        assertEquals(ImmutableMap.of(), requestBody.getParameters());
        assertNull(requestBody.getSemanticVersion());
        assertEquals(expectedResult, requestBody);
    }

    @Test
    void build_nullContentIds_exceptionIsThrown() {
        final ConfigurationException error = assertThrows(ConfigurationException.class,
            () -> new MultipleContentEndpoint(CONFIG, null));
        assertEquals( "Content IDs are not provided.", error.getMessage());
    }

    @Test
    void build_emptyContentIds_exceptionIsThrown() {
        final ConfigurationException error = assertThrows(ConfigurationException.class,
            () -> new MultipleContentEndpoint(CONFIG, ImmutableSet.of()));
        assertEquals( "Content IDs are not provided.", error.getMessage());
    }

    @Test
    void build_nullConfig_exceptionIsThrown() {
        final ConfigurationException error = assertThrows(ConfigurationException.class,
            () -> new MultipleContentEndpoint(null, ImmutableSet.of("111")));
        assertEquals( "Config is not provided.", error.getMessage());
    }

    @Test
    void formatJsonResponse_fullResponseWithoutErrors_fullResponseReturned() {
        final JsonNode jsonNode = JoystickUtil.readTree("{\"dev_test_001\":{\"data\":{\"config_name\":\"initial-test-config-dev-001\"}," +
            "\"hash\":\"2f5aa20f\",\"meta\":{\"uid\":0,\"mod\":0,\"variants\":[],\"seg\":[]}}," +
            "\"dev_test_002\":{\"data\":{\"config_name\":\"initial-test-config-dev-002\"}," +
            "\"hash\":\"2f5aa20f\",\"meta\":{\"uid\":0,\"mod\":0,\"variants\":[],\"seg\":[]}}}");

        MultipleContentEndpoint multipleContent = new MultipleContentEndpoint(CONFIG, ImmutableSet.of("id1", "id2"))
            .setFullResponse(true);

        final JsonNode result = multipleContent.formatJsonResponse(jsonNode);
        assertEquals(jsonNode, result);
    }

    @Test
    void formatJsonResponse_fullResponseWithErrors_multipleContentsApiExceptionThrown() {
        final JsonNode jsonNode = JoystickUtil.readTree("{\"dev_test_001\":{\"data\":{\"config_name\":\"initial-test-config-dev-001\"}," +
            "\"hash\":\"2f5aa20f\",\"meta\":{\"uid\":0,\"mod\":0,\"variants\":[],\"seg\":[]}}," +
            "\"dev_test_002\":\"Some error occurred\"}");

        MultipleContentEndpoint multipleContent = new MultipleContentEndpoint(CONFIG, ImmutableSet.of("id1", "id2"))
            .setFullResponse(true);

        final JsonNode resultErrorNode = JoystickUtil.readTree("{\"dev_test_002\":\"Some error occurred\"}");
        final MultipleContentsApiException error = assertThrows(MultipleContentsApiException.class,
            () -> multipleContent.formatJsonResponse(jsonNode));
        assertEquals( "Response from remote server contains errors:"
            + System.lineSeparator() + resultErrorNode.toPrettyString(), error.getMessage());
    }

    @Test
    void formatJsonResponse_shortResponseWithoutErrors_shortResponseReturned() {
        final JsonNode jsonNode = JoystickUtil.readTree("{\"dev_test_001\":{\"data\":{\"config_name\":\"initial-test-config-dev-001\"}," +
            "\"hash\":\"2f5aa20f\",\"meta\":{\"uid\":0,\"mod\":0,\"variants\":[],\"seg\":[]}}," +
            "\"dev_test_002\":{\"data\":{\"config_name\":\"initial-test-config-dev-002\"}," +
            "\"hash\":\"2f5aa20f\",\"meta\":{\"uid\":0,\"mod\":0,\"variants\":[],\"seg\":[]}}}");

        MultipleContentEndpoint multipleContent = new MultipleContentEndpoint(CONFIG, ImmutableSet.of("id1", "id2"))
            .setFullResponse(false);

        final JsonNode result = multipleContent.formatJsonResponse(jsonNode);
        assertEquals("{\"dev_test_001\":{\"config_name\":\"initial-test-config-dev-001\"}," +
            "\"dev_test_002\":{\"config_name\":\"initial-test-config-dev-002\"}}", result.toString());
    }

    @Test
    void formatJsonResponse_shortResponseWithErrors_multipleContentsApiExceptionThrown() {
        final JsonNode jsonNode = JoystickUtil.readTree("{\"dev_test_001\":{\"data\":{\"config_name\":\"initial-test-config-dev-001\"}," +
            "\"hash\":\"2f5aa20f\",\"meta\":{\"uid\":0,\"mod\":0,\"variants\":[],\"seg\":[]}}," +
            "\"dev_test_002\":\"Some error occurred\"}");

        MultipleContentEndpoint multipleContent = new MultipleContentEndpoint(CONFIG, ImmutableSet.of("id1", "id2"));

        final JsonNode resultErrorNode = JoystickUtil.readTree("{\"dev_test_002\":\"Some error occurred\"}");
        final MultipleContentsApiException error = assertThrows(MultipleContentsApiException.class,
            () -> multipleContent.formatJsonResponse(jsonNode));
        assertEquals( "Response from remote server contains errors:"
            + System.lineSeparator() + resultErrorNode.toPrettyString(), error.getMessage());
    }

}
