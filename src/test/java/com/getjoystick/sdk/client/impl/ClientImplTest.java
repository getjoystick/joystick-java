package com.getjoystick.sdk.client.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.getjoystick.sdk.cache.ApiCache;
import com.getjoystick.sdk.cache.impl.ApiCacheLRU;
import com.getjoystick.sdk.client.Client;
import com.getjoystick.sdk.client.ClientConfig;
import com.getjoystick.sdk.errors.ApiUnknownException;
import com.google.common.collect.ImmutableSet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.io.CloseMode;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ClientImplTest {

    private static final String API_KEY = "mocked-test-api-key";

    @Test
    void getContents_singleIdAndContentIsNotCached_callToJoystickViaSingleApi() throws Exception {
        try (MockedStatic<HttpClientBuilder> ignored = Mockito.mockStatic(HttpClientBuilder.class)) {
            final HttpClientBuilder httpClientBuilder = mock(HttpClientBuilder.class);
            when(HttpClientBuilder.create()).thenReturn(httpClientBuilder);

            doReturn(httpClientBuilder).when(httpClientBuilder).setDefaultRequestConfig(any());
            doReturn(httpClientBuilder).when(httpClientBuilder).setDefaultHeaders(any());

            final CloseableHttpClient httpClient = mock(CloseableHttpClient.class);
            doReturn(httpClient).when(httpClientBuilder).build();

            final String response = "{\"data\":\"{\\\"config_name\\\":\\\"initial-test-config-dev-001\\\"}\"," +
                "\"hash\":\"2f5aa20f\",\"meta\":{\"uid\":0,\"mod\":0,\"variants\":[],\"seg\":[]}}";
            final JsonNode node = mock(JsonNode.class);
            doReturn(response).when(node).toString();
            doReturn(node).when(httpClient)
                .execute(any(ClassicHttpRequest.class), any(HttpClientResponseHandler.class));
            final String result = new ClientImpl(ClientConfig.builder().setApiKey(API_KEY).build())
                .getContents(ImmutableSet.of("id1"));
            assertEquals(response, result);
        }
    }

    @Test
    void getContents_ioExceptionDuringCallToRemoteServer_apiUnknownExceptionThrown() throws Exception {
        try (MockedStatic<HttpClientBuilder> ignored = Mockito.mockStatic(HttpClientBuilder.class)) {
            final HttpClientBuilder httpClientBuilder = mock(HttpClientBuilder.class);
            when(HttpClientBuilder.create()).thenReturn(httpClientBuilder);

            doReturn(httpClientBuilder).when(httpClientBuilder).setDefaultRequestConfig(any());
            doReturn(httpClientBuilder).when(httpClientBuilder).setDefaultHeaders(any());

            final CloseableHttpClient httpClient = mock(CloseableHttpClient.class);
            doReturn(httpClient).when(httpClientBuilder).build();
            doThrow(IOException.class).when(httpClient)
                .execute(any(ClassicHttpRequest.class), any(HttpClientResponseHandler.class));

            final ApiUnknownException error = assertThrows(ApiUnknownException.class,
                () -> new ClientImpl(ClientConfig.builder().setApiKey(API_KEY).build())
                    .getContents(ImmutableSet.of("id1")));
            assertEquals( "Unable to complete the request", error.getMessage());
        }
    }

    @Test
    void getContents_contentExistsInCache_resultFromCache() throws Exception {
        try (MockedStatic<HttpClientBuilder> ignored = Mockito.mockStatic(HttpClientBuilder.class)) {
            final HttpClientBuilder httpClientBuilder = mock(HttpClientBuilder.class);
            when(HttpClientBuilder.create()).thenReturn(httpClientBuilder);

            doReturn(httpClientBuilder).when(httpClientBuilder).setDefaultRequestConfig(any());
            doReturn(httpClientBuilder).when(httpClientBuilder).setDefaultHeaders(any());

            final String response = "{\"data\":\"{\\\"config_name\\\":\\\"initial-test-config-dev-001\\\"}\"," +
                "\"hash\":\"2f5aa20f\",\"meta\":{\"uid\":0,\"mod\":0,\"variants\":[],\"seg\":[]}}";
            final ApiCache<String, String> cache = new ApiCacheLRU<>();
            cache.put("3630fd3bcb14a8b6c28d1b37a04cf09c84c404508459137f09e489db252a6f77", response);
            final ClientConfig clientConfig = ClientConfig.builder().setApiKey(API_KEY).setCache(cache).build();
            final String result = new ClientImpl(clientConfig).getContents(ImmutableSet.of("id1"));
            assertEquals(response, result);
        }
    }

    @Test
    void getContents_multipleContentIdsAndContentNotCached_callToJoystickViaMultipleApi() throws Exception {
        try (MockedStatic<HttpClientBuilder> ignored = Mockito.mockStatic(HttpClientBuilder.class)) {
            final HttpClientBuilder httpClientBuilder = mock(HttpClientBuilder.class);
            when(HttpClientBuilder.create()).thenReturn(httpClientBuilder);

            doReturn(httpClientBuilder).when(httpClientBuilder).setDefaultRequestConfig(any());
            doReturn(httpClientBuilder).when(httpClientBuilder).setDefaultHeaders(any());

            final CloseableHttpClient httpClient = mock(CloseableHttpClient.class);
            doReturn(httpClient).when(httpClientBuilder).build();

            final String response = "{\"data\":\"{\\\"config_name\\\":\\\"initial-test-config-dev-001\\\"}\"," +
                "\"hash\":\"2f5aa20f\",\"meta\":{\"uid\":0,\"mod\":0,\"variants\":[],\"seg\":[]}}";
            final JsonNode node = mock(JsonNode.class);
            doReturn(response).when(node).toString();
            doReturn(node).when(httpClient)
                .execute(any(ClassicHttpRequest.class), any(HttpClientResponseHandler.class));
            final String result = new ClientImpl(ClientConfig.builder().setApiKey(API_KEY).build())
                .getContents(ImmutableSet.of("id1","id2"));
            assertEquals(response, result);
        }
    }

    @Test
    void getContents_emptyIdsAsParam_exceptionIsThrown() {
        final IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
            () -> new ClientImpl(ClientConfig.builder().setApiKey(API_KEY).build())
                .getContents(ImmutableSet.of()));
        assertEquals( "No Content ID provided.", error.getMessage());
    }

    @Test
    void close_getContents_testAutoClosable() throws Exception {
        try (MockedStatic<HttpClientBuilder> ignored = Mockito.mockStatic(HttpClientBuilder.class)) {
            final CloseableHttpClient mockedClient = mock(CloseableHttpClient.class);
            final HttpClientBuilder httpClientBuilder = mock(HttpClientBuilder.class);
            when(HttpClientBuilder.create()).thenReturn(httpClientBuilder);

            doReturn(httpClientBuilder).when(httpClientBuilder).setDefaultRequestConfig(any());
            doReturn(httpClientBuilder).when(httpClientBuilder).setDefaultHeaders(any());
            doReturn(mockedClient).when(httpClientBuilder).build();

            final ClientConfig clientConfig = ClientConfig.builder().setApiKey(API_KEY).build();
            final Client client = new ClientImpl(clientConfig);
            client.close();
            verify(mockedClient).close(CloseMode.IMMEDIATE);
        }
    }

}
