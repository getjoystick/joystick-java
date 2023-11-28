package com.getjoystick.sdk.client.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;
import com.getjoystick.sdk.BaseTest;
import com.getjoystick.sdk.client.ClientConfig;
import com.getjoystick.sdk.errors.ApiBadRequestException;
import com.getjoystick.sdk.models.JoystickFullContent;
import com.getjoystick.sdk.util.JoystickMapper;
import com.google.common.collect.ImmutableSet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.HttpEntities;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ClientImplWithMockedJsonTest extends BaseTest {

    private static final String API_KEY = "mocked-test-api-key";

    @Test
    void getContent_noPermissions_exceptionIsThrown() throws IOException {
        String contentId = "id1";
        try (MockedStatic<HttpClientBuilder> ignored = Mockito.mockStatic(HttpClientBuilder.class)) {
            final HttpClientBuilder httpClientBuilder = mock(HttpClientBuilder.class);
            when(HttpClientBuilder.create()).thenReturn(httpClientBuilder);

            doReturn(httpClientBuilder).when(httpClientBuilder).setDefaultRequestConfig(any());
            doReturn(httpClientBuilder).when(httpClientBuilder).setDefaultHeaders(any());

            final CloseableHttpClient httpClient = mock(CloseableHttpClient.class);
            doReturn(httpClient).when(httpClientBuilder).build();

            // Mock the response behaviour and call a real method
            ClassicHttpResponse mockResponse = mock(ClassicHttpResponse.class);
            doReturn(HttpStatus.SC_UNAUTHORIZED).when(mockResponse).getCode();
            final String mockedResponseBody =
                toString("/com/getjoystick/sdk/client/impl/AccessDeniedResponse.json");
            HttpEntity mockEntity = HttpEntities.create(outputStream -> {
                new ObjectMapper().writeValue(outputStream, mockedResponseBody);
                outputStream.flush();
            }, ContentType.APPLICATION_JSON);
            doReturn(mockEntity).when(mockResponse).getEntity();

            doAnswer(invocation -> {
                HttpClientResponseHandler<?> handler = invocation.getArgument(1);
                return handler.handleResponse(mockResponse);
            })
                .when(httpClient)
                .execute(any(ClassicHttpRequest.class), any(HttpClientResponseHandler.class));
            final String INCORRECT_API_KEY = "incorrect-key";
            final ApiBadRequestException error = assertThrows(ApiBadRequestException.class,
                () -> new ClientImpl(ClientConfig.builder().setApiKey(INCORRECT_API_KEY).build())
                    .getContent(contentId)
            );
            assertEquals("HTTP/1.1 401 ", error.getMessage());
            // Check request values
            checkRequiredHeaders(httpClientBuilder, INCORRECT_API_KEY);
            checkRequestURL(httpClient, "POST", "/api/v1/config/id1/dynamic");
        }
    }

    @Test
    void getContent_validConfig_validResultReturned() throws IOException {
        String contentId = "id1";
        try (MockedStatic<HttpClientBuilder> ignored = Mockito.mockStatic(HttpClientBuilder.class)) {
            final HttpClientBuilder httpClientBuilder = mock(HttpClientBuilder.class);
            when(HttpClientBuilder.create()).thenReturn(httpClientBuilder);

            doReturn(httpClientBuilder).when(httpClientBuilder).setDefaultRequestConfig(any());
            doReturn(httpClientBuilder).when(httpClientBuilder).setDefaultHeaders(any());

            final CloseableHttpClient httpClient = mock(CloseableHttpClient.class);
            doReturn(httpClient).when(httpClientBuilder).build();

            // Mock the response behaviour and call a real method
            ClassicHttpResponse mockResponse = mock(ClassicHttpResponse.class);
            doReturn(HttpStatus.SC_OK).when(mockResponse).getCode();
            HttpEntity mockEntity = HttpEntities.create(
                toString("/com/getjoystick/sdk/client/impl/FullResponseSample.json"),
                ContentType.APPLICATION_JSON
            );
            doReturn(mockEntity).when(mockResponse).getEntity();

            doAnswer(invocation -> {
                HttpClientResponseHandler<?> handler = invocation.getArgument(1);
                return handler.handleResponse(mockResponse);
            })
                .when(httpClient)
                .execute(any(ClassicHttpRequest.class), any(HttpClientResponseHandler.class));

            final JsonNode content = new ClientImpl(ClientConfig.builder().setApiKey(API_KEY).build())
                .getContent(contentId);

            final Map<String, ?> contentMap = JoystickMapper.treeToValue(content, Map.class);
            assertNotNull(contentMap);
            assertEquals(2, contentMap.size());
            assertEquals("initial-test-config-dev-001", contentMap.get("config_name"));
            assertEquals("unit-test", contentMap.get("from_location"));
            // Check request values
            checkRequiredHeaders(httpClientBuilder, API_KEY);
            checkRequestURL(httpClient, "POST", "/api/v1/config/id1/dynamic");
        }
    }

    @Test
    void getContent_validConfigWithSerializedParam_contentDataIsInSerializedForm() throws IOException {
        String contentId = "id1";
        try (MockedStatic<HttpClientBuilder> ignored = Mockito.mockStatic(HttpClientBuilder.class)) {
            final HttpClientBuilder httpClientBuilder = mock(HttpClientBuilder.class);
            when(HttpClientBuilder.create()).thenReturn(httpClientBuilder);

            doReturn(httpClientBuilder).when(httpClientBuilder).setDefaultRequestConfig(any());
            doReturn(httpClientBuilder).when(httpClientBuilder).setDefaultHeaders(any());

            final CloseableHttpClient httpClient = mock(CloseableHttpClient.class);
            doReturn(httpClient).when(httpClientBuilder).build();

            // Mock the response behaviour and call a real method
            ClassicHttpResponse mockResponse = mock(ClassicHttpResponse.class);
            doReturn(HttpStatus.SC_OK).when(mockResponse).getCode();
            HttpEntity mockEntity = HttpEntities.create(
                toString("/com/getjoystick/sdk/client/impl/SerializedResponseSample.json"),
                ContentType.APPLICATION_JSON
            );
            doReturn(mockEntity).when(mockResponse).getEntity();

            doAnswer(invocation -> {
                HttpClientResponseHandler<?> handler = invocation.getArgument(1);
                return handler.handleResponse(mockResponse);
            })
                .when(httpClient)
                .execute(any(ClassicHttpRequest.class), any(HttpClientResponseHandler.class));

            final JsonNode joystickContent = new ClientImpl(
                ClientConfig.builder().setApiKey(API_KEY).setSerialized(true).build())
                .getContent(contentId);

            final String content = joystickContent.toString();
            assertNotNull(content);
            assertEquals("\"{\\\"config_name\\\":\\\"initial-test-config-dev-001\\\",\\\"from_location\\\":\\\"unit-test\\\"}\"",
                content);
            // Check request values
            checkRequiredHeaders(httpClientBuilder, API_KEY);
            checkRequestURL(httpClient, "POST", "/api/v1/config/id1/dynamic?responseType=serialized");
        }
    }


    @Test
    void getFullContentSerialized_validConfig_contentReturnedInSerializedForm() throws IOException {
        String contentId = "id1";
        try (MockedStatic<HttpClientBuilder> ignored = Mockito.mockStatic(HttpClientBuilder.class)) {
            final HttpClientBuilder httpClientBuilder = mock(HttpClientBuilder.class);
            when(HttpClientBuilder.create()).thenReturn(httpClientBuilder);

            doReturn(httpClientBuilder).when(httpClientBuilder).setDefaultRequestConfig(any());
            doReturn(httpClientBuilder).when(httpClientBuilder).setDefaultHeaders(any());

            final CloseableHttpClient httpClient = mock(CloseableHttpClient.class);
            doReturn(httpClient).when(httpClientBuilder).build();

            // Mock the response behaviour and call a real method
            ClassicHttpResponse mockResponse = mock(ClassicHttpResponse.class);
            doReturn(HttpStatus.SC_OK).when(mockResponse).getCode();
            HttpEntity mockEntity = HttpEntities.create(
                toString("/com/getjoystick/sdk/client/impl/SerializedResponseStore.json"),
                ContentType.APPLICATION_JSON
            );
            doReturn(mockEntity).when(mockResponse).getEntity();

            doAnswer(invocation -> {
                HttpClientResponseHandler<?> handler = invocation.getArgument(1);
                return handler.handleResponse(mockResponse);
            })
                .when(httpClient)
                .execute(any(ClassicHttpRequest.class), any(HttpClientResponseHandler.class));

            final JoystickFullContent<String> joystickContent = new ClientImpl(
                ClientConfig.builder().setApiKey(API_KEY).setSerialized(true).build())
                .getFullContentSerialized(contentId);

            final String content = joystickContent.getData();
            assertNotNull(content);
            assertEquals("{\\\"store\\\":{\\\"book\\\":[{\\\"category\\\":\\\"Version2\\\",\\\"author\\\":\\\"Nigel Rees\\\",\\\"title\\\":\\\"Sayings of the Century\\\",\\\"price\\\":8.95},{\\\"category\\\":\\\"fiction\\\",\\\"author\\\":\\\"Evelyn Waugh\\\",\\\"title\\\":\\\"Sword of Honour\\\",\\\"price\\\":12.99},{\\\"category\\\":\\\"fiction\\\",\\\"author\\\":\\\"J. R. R. Tolkien\\\",\\\"title\\\":\\\"The Lord of the Rings\\\",\\\"isbn\\\":\\\"0-395-19395-8\\\",\\\"price\\\":22.99}],\\\"bicycle\\\":{\\\"color\\\":\\\"red\\\",\\\"price\\\":19.95}}}",
                content);
            // Check request values
            checkRequiredHeaders(httpClientBuilder, API_KEY);
            checkRequestURL(httpClient, "POST", "/api/v1/config/id1/dynamic?responseType=serialized");
        }
    }

    @Test
    void getContentSerialized_validConfig_contentReturnedInSerializedForm() throws IOException {
        String contentId = "id1";
        try (MockedStatic<HttpClientBuilder> ignored = Mockito.mockStatic(HttpClientBuilder.class)) {
            final HttpClientBuilder httpClientBuilder = mock(HttpClientBuilder.class);
            when(HttpClientBuilder.create()).thenReturn(httpClientBuilder);

            doReturn(httpClientBuilder).when(httpClientBuilder).setDefaultRequestConfig(any());
            doReturn(httpClientBuilder).when(httpClientBuilder).setDefaultHeaders(any());

            final CloseableHttpClient httpClient = mock(CloseableHttpClient.class);
            doReturn(httpClient).when(httpClientBuilder).build();

            // Mock the response behaviour and call a real method
            ClassicHttpResponse mockResponse = mock(ClassicHttpResponse.class);
            doReturn(HttpStatus.SC_OK).when(mockResponse).getCode();
            HttpEntity mockEntity = HttpEntities.create(
                toString("/com/getjoystick/sdk/client/impl/SerializedResponseStore.json"),
                ContentType.APPLICATION_JSON
            );
            doReturn(mockEntity).when(mockResponse).getEntity();

            doAnswer(invocation -> {
                HttpClientResponseHandler<?> handler = invocation.getArgument(1);
                return handler.handleResponse(mockResponse);
            })
                .when(httpClient)
                .execute(any(ClassicHttpRequest.class), any(HttpClientResponseHandler.class));

            final String content = new ClientImpl(
                ClientConfig.builder().setApiKey(API_KEY).setSerialized(true).build())
                .getContentSerialized(contentId);

            assertNotNull(content);
            assertEquals("{\\\"store\\\":{\\\"book\\\":[{\\\"category\\\":\\\"Version2\\\",\\\"author\\\":\\\"Nigel Rees\\\",\\\"title\\\":\\\"Sayings of the Century\\\",\\\"price\\\":8.95},{\\\"category\\\":\\\"fiction\\\",\\\"author\\\":\\\"Evelyn Waugh\\\",\\\"title\\\":\\\"Sword of Honour\\\",\\\"price\\\":12.99},{\\\"category\\\":\\\"fiction\\\",\\\"author\\\":\\\"J. R. R. Tolkien\\\",\\\"title\\\":\\\"The Lord of the Rings\\\",\\\"isbn\\\":\\\"0-395-19395-8\\\",\\\"price\\\":22.99}],\\\"bicycle\\\":{\\\"color\\\":\\\"red\\\",\\\"price\\\":19.95}}}",
                content);
            // Check request values
            checkRequiredHeaders(httpClientBuilder, API_KEY);
            checkRequestURL(httpClient, "POST", "/api/v1/config/id1/dynamic?responseType=serialized");
        }
    }

    @Test
    void getFullContent_validConfig_fullContentReturned() throws IOException {
        String contentId = "id1";
        try (MockedStatic<HttpClientBuilder> ignored = Mockito.mockStatic(HttpClientBuilder.class)) {
            final HttpClientBuilder httpClientBuilder = mock(HttpClientBuilder.class);
            when(HttpClientBuilder.create()).thenReturn(httpClientBuilder);

            doReturn(httpClientBuilder).when(httpClientBuilder).setDefaultRequestConfig(any());
            doReturn(httpClientBuilder).when(httpClientBuilder).setDefaultHeaders(any());

            final CloseableHttpClient httpClient = mock(CloseableHttpClient.class);
            doReturn(httpClient).when(httpClientBuilder).build();

            // Mock the response behaviour and call a real method
            ClassicHttpResponse mockResponse = mock(ClassicHttpResponse.class);
            doReturn(HttpStatus.SC_OK).when(mockResponse).getCode();
            HttpEntity mockEntity = HttpEntities.create(
                toString("/com/getjoystick/sdk/client/impl/SerializedResponseStore.json"),
                ContentType.APPLICATION_JSON
            );
            doReturn(mockEntity).when(mockResponse).getEntity();

            doAnswer(invocation -> {
                HttpClientResponseHandler<?> handler = invocation.getArgument(1);
                return handler.handleResponse(mockResponse);
            })
                .when(httpClient)
                .execute(any(ClassicHttpRequest.class), any(HttpClientResponseHandler.class));

            final JoystickFullContent<JsonNode> fullContent = new ClientImpl(
                ClientConfig.builder().setApiKey(API_KEY).setSerialized(true).build())
                .getFullContent(contentId);

            assertNotNull(fullContent);
            JoystickMapper.readTree("{\"speed\":20,\"name\":\"Turbo\",\"size\":245,\"price\":22.99}");
            assertEquals(new TextNode("{\"store\":{\"book\":[{\"category\":\"Version2\",\"author\":" +
                        "\"Nigel Rees\",\"title\":\"Sayings of the Century\",\"price\":8.95},{\"category\":\"fiction\"," +
                        "\"author\":\"Evelyn Waugh\",\"title\":\"Sword of Honour\",\"price\":12.99},{\"category\":" +
                        "\"fiction\",\"author\":\"J. R. R. Tolkien\",\"title\":\"The Lord of the Rings\",\"isbn\":" +
                        "\"0-395-19395-8\",\"price\":22.99}],\"bicycle\":{\"color\":\"red\",\"price\":19.95}}}"),
                fullContent.getData());
            // Check request values
            checkRequiredHeaders(httpClientBuilder, API_KEY);
            checkRequestURL(httpClient, "POST", "/api/v1/config/id1/dynamic?responseType=serialized");
        }
    }

    @Test
    void getFullContents_validConfig_fullContentsMapReturned() throws IOException {
        try (MockedStatic<HttpClientBuilder> ignored = Mockito.mockStatic(HttpClientBuilder.class)) {
            final HttpClientBuilder httpClientBuilder = mock(HttpClientBuilder.class);
            when(HttpClientBuilder.create()).thenReturn(httpClientBuilder);

            doReturn(httpClientBuilder).when(httpClientBuilder).setDefaultRequestConfig(any());
            doReturn(httpClientBuilder).when(httpClientBuilder).setDefaultHeaders(any());

            final CloseableHttpClient httpClient = mock(CloseableHttpClient.class);
            doReturn(httpClient).when(httpClientBuilder).build();

            // Mock the response behaviour and call a real method
            ClassicHttpResponse mockResponse = mock(ClassicHttpResponse.class);
            doReturn(HttpStatus.SC_OK).when(mockResponse).getCode();
            HttpEntity mockEntity = HttpEntities.create(
                toString("/com/getjoystick/sdk/client/impl/MultipleConfigSucessResponse.json"),
                ContentType.APPLICATION_JSON
            );
            doReturn(mockEntity).when(mockResponse).getEntity();

            doAnswer(invocation -> {
                HttpClientResponseHandler<?> handler = invocation.getArgument(1);
                return handler.handleResponse(mockResponse);
            })
                .when(httpClient)
                .execute(any(ClassicHttpRequest.class), any(HttpClientResponseHandler.class));

            final Map<String, JoystickFullContent<JsonNode>> fullContents = new ClientImpl(
                ClientConfig.builder().setApiKey(API_KEY).setSerialized(true).build())
                .getFullContents(ImmutableSet.of("race_config", "horror_config"));

            assertNotNull(fullContents);
            assertEquals("{\"data\":{\"speed\":20,\"name\":\"Turbo\",\"size\":245,\"price\":22.99},\"hash\":\"c272ef04\",\"meta\":{\"uid\":0,\"mod\":0,\"variants\":[],\"seg\":[]}}",
                fullContents.get("race_config").toString());
            assertEquals("{\"data\":{\"level\":133,\"mode\":\"Hard\",\"age\":18,\"price\":33.99},\"hash\":\"e10325c5\",\"meta\":{\"uid\":0,\"mod\":0,\"variants\":[],\"seg\":[]}}",
                fullContents.get("horror_config").toString());
            // Check request values
            checkRequiredHeaders(httpClientBuilder, API_KEY);
            checkRequestURL(httpClient, "POST", "/api/v1/combine/?dynamic=true&responseType=serialized&c=%5B%22race_config%22%2C%22horror_config%22%5D");
        }
    }

    @Test
    void getContents_validConfig_contentsMapReturned() throws IOException {
        try (MockedStatic<HttpClientBuilder> ignored = Mockito.mockStatic(HttpClientBuilder.class)) {
            final HttpClientBuilder httpClientBuilder = mock(HttpClientBuilder.class);
            when(HttpClientBuilder.create()).thenReturn(httpClientBuilder);

            doReturn(httpClientBuilder).when(httpClientBuilder).setDefaultRequestConfig(any());
            doReturn(httpClientBuilder).when(httpClientBuilder).setDefaultHeaders(any());

            final CloseableHttpClient httpClient = mock(CloseableHttpClient.class);
            doReturn(httpClient).when(httpClientBuilder).build();

            // Mock the response behaviour and call a real method
            ClassicHttpResponse mockResponse = mock(ClassicHttpResponse.class);
            doReturn(HttpStatus.SC_OK).when(mockResponse).getCode();
            HttpEntity mockEntity = HttpEntities.create(
                toString("/com/getjoystick/sdk/client/impl/MultipleConfigSucessResponse.json"),
                ContentType.APPLICATION_JSON
            );
            doReturn(mockEntity).when(mockResponse).getEntity();

            doAnswer(invocation -> {
                HttpClientResponseHandler<?> handler = invocation.getArgument(1);
                return handler.handleResponse(mockResponse);
            })
                .when(httpClient)
                .execute(any(ClassicHttpRequest.class), any(HttpClientResponseHandler.class));

            final Map<String, JsonNode> contentMap = new ClientImpl(
                ClientConfig.builder().setApiKey(API_KEY).setSerialized(true).build())
                .getContents(ImmutableSet.of("race_config", "horror_config"));

            assertNotNull(contentMap);
            assertEquals("{\"speed\":20,\"name\":\"Turbo\",\"size\":245,\"price\":22.99}",
                contentMap.get("race_config").toString());
            assertEquals("{\"level\":133,\"mode\":\"Hard\",\"age\":18,\"price\":33.99}",
                contentMap.get("horror_config").toString());
            // Check request values
            checkRequiredHeaders(httpClientBuilder, API_KEY);
            checkRequestURL(httpClient, "POST", "/api/v1/combine/?dynamic=true&responseType=serialized&c=%5B%22race_config%22%2C%22horror_config%22%5D");
        }
    }

    @Test
    void getContentsSerialized_validConfig_serializedContentsMapReturned() throws IOException {
        try (MockedStatic<HttpClientBuilder> ignored = Mockito.mockStatic(HttpClientBuilder.class)) {
            final HttpClientBuilder httpClientBuilder = mock(HttpClientBuilder.class);
            when(HttpClientBuilder.create()).thenReturn(httpClientBuilder);

            doReturn(httpClientBuilder).when(httpClientBuilder).setDefaultRequestConfig(any());
            doReturn(httpClientBuilder).when(httpClientBuilder).setDefaultHeaders(any());

            final CloseableHttpClient httpClient = mock(CloseableHttpClient.class);
            doReturn(httpClient).when(httpClientBuilder).build();

            // Mock the response behaviour and call a real method
            ClassicHttpResponse mockResponse = mock(ClassicHttpResponse.class);
            doReturn(HttpStatus.SC_OK).when(mockResponse).getCode();
            HttpEntity mockEntity = HttpEntities.create(
                toString("/com/getjoystick/sdk/client/impl/MultipleConfigSerializedSucessResponse.json"),
                ContentType.APPLICATION_JSON
            );
            doReturn(mockEntity).when(mockResponse).getEntity();

            doAnswer(invocation -> {
                HttpClientResponseHandler<?> handler = invocation.getArgument(1);
                return handler.handleResponse(mockResponse);
            })
                .when(httpClient)
                .execute(any(ClassicHttpRequest.class), any(HttpClientResponseHandler.class));

            final Map<String, String> contentMap = new ClientImpl(
                ClientConfig.builder().setApiKey(API_KEY).setSerialized(true).build())
                .getContentsSerialized(ImmutableSet.of("race_config", "horror_config"));

            assertNotNull(contentMap);
            assertEquals("{\\\"speed\\\":20,\\\"name\\\":\\\"Turbo\\\",\\\"size\\\":245,\\\"price\\\":22.99}",
                contentMap.get("race_config"));
            assertEquals("{\\\"level\\\":133,\\\"mode\\\":\\\"Hard\\\",\\\"age\\\":18,\\\"price\\\":33.99}",
                contentMap.get("horror_config"));
            // Check request values
            checkRequiredHeaders(httpClientBuilder, API_KEY);
            checkRequestURL(httpClient, "POST", "/api/v1/combine/?dynamic=true&responseType=serialized&c=%5B%22race_config%22%2C%22horror_config%22%5D");
        }
    }

    @Test
    void getFullContentsSerialized_validConfig_serializedFullContentsMapReturned() throws IOException {
        try (MockedStatic<HttpClientBuilder> ignored = Mockito.mockStatic(HttpClientBuilder.class)) {
            final HttpClientBuilder httpClientBuilder = mock(HttpClientBuilder.class);
            when(HttpClientBuilder.create()).thenReturn(httpClientBuilder);

            doReturn(httpClientBuilder).when(httpClientBuilder).setDefaultRequestConfig(any());
            doReturn(httpClientBuilder).when(httpClientBuilder).setDefaultHeaders(any());

            final CloseableHttpClient httpClient = mock(CloseableHttpClient.class);
            doReturn(httpClient).when(httpClientBuilder).build();

            // Mock the response behaviour and call a real method
            ClassicHttpResponse mockResponse = mock(ClassicHttpResponse.class);
            doReturn(HttpStatus.SC_OK).when(mockResponse).getCode();
            HttpEntity mockEntity = HttpEntities.create(
                toString("/com/getjoystick/sdk/client/impl/MultipleConfigSerializedSucessResponse.json"),
                ContentType.APPLICATION_JSON
            );
            doReturn(mockEntity).when(mockResponse).getEntity();

            doAnswer(invocation -> {
                HttpClientResponseHandler<?> handler = invocation.getArgument(1);
                return handler.handleResponse(mockResponse);
            })
                .when(httpClient)
                .execute(any(ClassicHttpRequest.class), any(HttpClientResponseHandler.class));

            final Map<String, JoystickFullContent<String>> contentMap = new ClientImpl(
                ClientConfig.builder().setApiKey(API_KEY).setSerialized(true).build())
                .getFullContentsSerialized(ImmutableSet.of("race_config", "horror_config"));

            assertNotNull(contentMap);
            assertEquals("{\\\"speed\\\":20,\\\"name\\\":\\\"Turbo\\\",\\\"size\\\":245,\\\"price\\\":22.99}",
                contentMap.get("race_config").getData());
            assertEquals("{\"data\":\"{\\\"speed\\\":20,\\\"name\\\":\\\"Turbo\\\",\\\"size\\\":245,\\\"price\\\":22.99}\",\"hash\":\"c272ef04\",\"meta\":{\"uid\":0,\"mod\":0,\"variants\":[],\"seg\":[]}}",
                contentMap.get("race_config").toString());
            assertEquals("{\\\"level\\\":133,\\\"mode\\\":\\\"Hard\\\",\\\"age\\\":18,\\\"price\\\":33.99}",
                contentMap.get("horror_config").getData());
            // Check request values
            checkRequiredHeaders(httpClientBuilder, API_KEY);
            checkRequestURL(httpClient, "POST", "/api/v1/combine/?dynamic=true&responseType=serialized&c=%5B%22race_config%22%2C%22horror_config%22%5D");
        }
    }

    /**
     * Simple check request URL.
     *
     * @param client      to capture
     * @param expectedUri Ex.: "/api/v1/config/id1/dynamic?responseType=serialized"
     * @throws IOException
     */
    private void checkRequestURL(final CloseableHttpClient client, final String method, final String expectedUri)
        throws IOException {

        ArgumentCaptor<ClassicHttpRequest> reqCaptor = ArgumentCaptor.forClass(ClassicHttpRequest.class);
        verify(client).execute(reqCaptor.capture(), any(HttpClientResponseHandler.class));
        ClassicHttpRequest request = reqCaptor.getValue();
        assertEquals(method, request.getMethod());
        assertEquals(expectedUri, request.getRequestUri());
    }

    private void checkRequiredHeaders(final HttpClientBuilder clientBuilder, final String apiKey) {
        ArgumentCaptor<List<Header>> headersCaptor = ArgumentCaptor.forClass(List.class);
        verify(clientBuilder).setDefaultHeaders(headersCaptor.capture());
        List<Header> headers = headersCaptor.getValue();
        boolean hasValidHeaders = headers.stream().anyMatch(h -> "x-api-key".equals(h.getName()) && apiKey.equals(h.getValue()))
            &&
            headers.stream()
                .anyMatch(h -> "Content-Type".equals(h.getName())
                    && ContentType.APPLICATION_JSON.getMimeType().equals(h.getValue()));
        assertTrue(hasValidHeaders);
    }

}
