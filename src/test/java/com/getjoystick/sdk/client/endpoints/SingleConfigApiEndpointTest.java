package com.getjoystick.sdk.client.endpoints;

import com.getjoystick.sdk.client.ClientConfig;
import com.getjoystick.sdk.errors.ConfigurationException;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SingleConfigApiEndpointTest {

    private static final ClientConfig CONFIG = ClientConfig.builder().setApiKey("test-api-key").build();

    @Test
    void getQueryParameters_allParamsSet_paramOrderAndValuesCorrect() {
        String contentId = "id1";
        AbstractApiEndpoint singleConfigApiEndpoint = new SingleContentEndpoint(CONFIG, contentId)
            .setSerialized(true);
        NameValuePair[] queryParams = singleConfigApiEndpoint.getQueryParameters();
        assertEquals(new BasicNameValuePair("responseType", "serialized"), queryParams[0]);
        assertEquals(1, queryParams.length);
    }

    @Test
    void getQueryParameters_onlyContentIdsSet_emptyParams() {
        String contentId = "id1";
        AbstractApiEndpoint singleConfigApiEndpoint = new SingleContentEndpoint(CONFIG, contentId);
        NameValuePair[] queryParams = singleConfigApiEndpoint.getQueryParameters();
        assertNotNull(queryParams);
        assertEquals(0, queryParams.length);
    }

    @Test
    void getQueryParameters_serializedIsFalse_emptyParams() {
        String contentId = "id1";
        AbstractApiEndpoint singleConfigApiEndpoint = new SingleContentEndpoint(CONFIG, contentId)
            .setSerialized(false);
        NameValuePair[] queryParams = singleConfigApiEndpoint.getQueryParameters();
        assertNotNull(queryParams);
        assertEquals(0, queryParams.length);
    }

    @Test
    void getUrl_validContentId_urlIsCorrect() {
        String contentId = "id1";
        AbstractApiEndpoint singleConfigApiEndpoint = new SingleContentEndpoint(CONFIG, contentId)
            .setSerialized(true);
        NameValuePair[] queryParams = singleConfigApiEndpoint.getQueryParameters();
        assertEquals("https://api.getjoystick.com/api/v1/config/id1/dynamic",
            singleConfigApiEndpoint.getUrl());
    }

    @Test
    void build_nullContentIds_exceptionIsThrown() {
        final ConfigurationException error = assertThrows(ConfigurationException.class,
            () -> new SingleContentEndpoint(CONFIG, null));
        assertEquals( "Content ID is not provided.", error.getMessage());
    }

    @Test
    void build_emptyContentIds_exceptionIsThrown() {
        final ConfigurationException error = assertThrows(ConfigurationException.class,
            () -> new SingleContentEndpoint(CONFIG, ""));
        assertEquals( "Content ID is not provided.", error.getMessage());
    }

    @Test
    void build_nullConfig_exceptionIsThrown() {
        final ConfigurationException error = assertThrows(ConfigurationException.class,
            () -> new SingleContentEndpoint(null, "123"));
        assertEquals( "Config is not provided.", error.getMessage());
    }
}
