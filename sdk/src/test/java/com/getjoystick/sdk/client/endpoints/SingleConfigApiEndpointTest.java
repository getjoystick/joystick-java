package com.getjoystick.sdk.client.endpoints;

import com.getjoystick.sdk.errors.ConfigurationException;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SingleConfigApiEndpointTest {

    @Test
    void getQueryParameters_allParamsSet_paramOrderAndValuesCorrect() {
        String contentId = "id1";
        AbstractApiEndpoint singleConfigApiEndpoint = SingleContentEndpoint.builder()
            .setContentId(contentId)
            .setSerialized(true)
            .build();
        NameValuePair[] queryParams = singleConfigApiEndpoint.getQueryParameters();
        assertEquals(new BasicNameValuePair("responseType", "serialized"), queryParams[0]);
        assertEquals(1, queryParams.length);
    }

    @Test
    void getQueryParameters_onlyContentIdsSet_emptyParams() {
        String contentId = "id1";
        AbstractApiEndpoint singleConfigApiEndpoint = SingleContentEndpoint.builder()
            .setContentId(contentId)
            .build();
        NameValuePair[] queryParams = singleConfigApiEndpoint.getQueryParameters();
        assertNotNull(queryParams);
        assertEquals(0, queryParams.length);
    }

    @Test
    void getQueryParameters_serializedIsFalse_emptyParams() {
        String contentId = "id1";
        AbstractApiEndpoint singleConfigApiEndpoint = SingleContentEndpoint.builder()
            .setContentId(contentId)
            .setSerialized(false)
            .build();
        NameValuePair[] queryParams = singleConfigApiEndpoint.getQueryParameters();
        assertNotNull(queryParams);
        assertEquals(0, queryParams.length);
    }

    @Test
    void getUrl_validContentId_urlIsCorrect() {
        String contentId = "id1";
        AbstractApiEndpoint singleConfigApiEndpoint = SingleContentEndpoint.builder()
            .setContentId(contentId)
            .setSerialized(true)
            .build();
        NameValuePair[] queryParams = singleConfigApiEndpoint.getQueryParameters();
        assertEquals("https://api.getjoystick.com/api/v1/config/id1/dynamic",
            singleConfigApiEndpoint.getUrl());
    }

    @Test
    void build_nullContentIds_exceptionIsThrown() {
        final ConfigurationException error = assertThrows(ConfigurationException.class,
            () -> SingleContentEndpoint.builder().build());
        assertEquals( "Content ID is not provided.", error.getMessage());
    }

    @Test
    void build_emptyContentIds_exceptionIsThrown() {
        final ConfigurationException error = assertThrows(ConfigurationException.class,
            () -> SingleContentEndpoint.builder().setContentId("").build());
        assertEquals( "Content ID is not provided.", error.getMessage());
    }
}
