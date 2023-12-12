package com.getjoystick.sdk.client.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.getjoystick.sdk.cache.ApiCache;
import com.getjoystick.sdk.cache.impl.ApiCacheLRU;
import com.getjoystick.sdk.client.Client;
import com.getjoystick.sdk.client.ClientConfig;
import com.getjoystick.sdk.errors.ApiBadRequestException;
import com.getjoystick.sdk.errors.ApiUnknownException;
import com.getjoystick.sdk.models.PublishData;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.HttpEntities;
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
                .getContentsAsString(ImmutableSet.of("id1"));
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
                    .getContentsAsString(ImmutableSet.of("id1")));
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
            cache.put("9cc2c024dfa829d84772d874d1281299e7e9cab22fcb99a72009a10c3053e1ac", response);
            final ClientConfig clientConfig = ClientConfig.builder().setApiKey(API_KEY).setCache(cache).build();
            final String result = new ClientImpl(clientConfig).getContentsAsString(ImmutableSet.of("id1"));
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
                .getContentsAsString(ImmutableSet.of("id1","id2"));
            assertEquals(response, result);
        }
    }

    @Test
    void getContents_emptyIdsAsParam_exceptionIsThrown() {
        final IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
            () -> new ClientImpl(ClientConfig.builder().setApiKey(API_KEY).build())
                .getContentsAsString(ImmutableSet.of()));
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

    @Test
    void publishContentUpdate_correctUpdateData_success() throws Exception {
        try (MockedStatic<HttpClientBuilder> ignored = Mockito.mockStatic(HttpClientBuilder.class)) {
            final HttpClientBuilder httpClientBuilder = mock(HttpClientBuilder.class);
            when(HttpClientBuilder.create()).thenReturn(httpClientBuilder);

            doReturn(httpClientBuilder).when(httpClientBuilder).setDefaultRequestConfig(any());
            doReturn(httpClientBuilder).when(httpClientBuilder).setDefaultHeaders(any());

            final CloseableHttpClient httpClient = mock(CloseableHttpClient.class);
            doReturn(httpClient).when(httpClientBuilder).build();

            final String mockedResponseBody = "{\"data\":false,\"status\":1,\"message\":\"Success\"}";
            ClassicHttpResponse mockResponse = mock(ClassicHttpResponse.class);
            HttpEntity mockEntity = HttpEntities.create(outputStream -> {
                new ObjectMapper().writeValue(outputStream, mockedResponseBody);
                outputStream.flush();
            }, ContentType.APPLICATION_JSON);
            doReturn(HttpStatus.SC_OK).when(mockResponse).getCode();
            doReturn(mockEntity).when(mockResponse).getEntity();

            doAnswer(invocation -> {
                HttpClientResponseHandler handler = (HttpClientResponseHandler) invocation.getArguments()[1];
                return handler.handleResponse(mockResponse);
            })
                .when(httpClient)
                .execute(any(ClassicHttpRequest.class), any(HttpClientResponseHandler.class));
            try (Client client = new ClientImpl(ClientConfig.builder().setApiKey(API_KEY).build())) {
                String contentId = "id1";
                PublishData data = PublishData.builder()
                    .setDescription("test 1")
                    .setContent(ImmutableMap.of("k1", "v1", "k2", "v2"))
                    .build();
                client.publishContentUpdate(contentId, data);
            }
            verify(httpClient).execute(any(ClassicHttpRequest.class), any(HttpClientResponseHandler.class));
        }
    }

    @Test
    void publishContentUpdate_WhenForbiddenToUpdate() throws Exception {
        try (MockedStatic<HttpClientBuilder> ignored = Mockito.mockStatic(HttpClientBuilder.class)) {
            final HttpClientBuilder httpClientBuilder = mock(HttpClientBuilder.class);
            when(HttpClientBuilder.create()).thenReturn(httpClientBuilder);

            doReturn(httpClientBuilder).when(httpClientBuilder).setDefaultRequestConfig(any());
            doReturn(httpClientBuilder).when(httpClientBuilder).setDefaultHeaders(any());

            final CloseableHttpClient httpClient = mock(CloseableHttpClient.class);
            doReturn(httpClient).when(httpClientBuilder).build();

            ClassicHttpResponse mockResponse = mock(ClassicHttpResponse.class);
            doReturn(HttpStatus.SC_FORBIDDEN).when(mockResponse).getCode();

            doAnswer(invocation -> {
                HttpClientResponseHandler handler = (HttpClientResponseHandler) invocation.getArguments()[1];
                return handler.handleResponse(mockResponse);
            })
                .when(httpClient)
                .execute(any(ClassicHttpRequest.class), any(HttpClientResponseHandler.class));
            try (Client client = new ClientImpl(ClientConfig.builder().setApiKey(API_KEY).build())) {
                String contentId = "id1";
                PublishData data = PublishData.builder()
                    .setDescription("test 1")
                    .setContent(ImmutableMap.of("k1", "v1", "k2", "v2"))
                    .build();
                final ApiBadRequestException error = assertThrows(ApiBadRequestException.class,
                    () -> client.publishContentUpdate(contentId, data));
                assertEquals("HTTP/1.1 403 ", error.getMessage());
            }
            verify(httpClient).execute(any(ClassicHttpRequest.class), any(HttpClientResponseHandler.class));
        }
    }
}
