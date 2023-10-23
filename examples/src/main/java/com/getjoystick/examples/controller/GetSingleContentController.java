package com.getjoystick.examples.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.getjoystick.examples.model.ClientConfigDto;
import com.getjoystick.sdk.Joystick;
import com.getjoystick.sdk.client.Client;
import com.getjoystick.sdk.client.ClientConfig;
import com.getjoystick.sdk.models.JoystickContent;
import com.getjoystick.sdk.models.JoystickFullContent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GetSingleContentController {

    @GetMapping("/single/content")
    public String getContent(@RequestParam final String apiKey,
                             @RequestParam final String contentId) {
        ClientConfig config = ClientConfig.builder().setApiKey(apiKey).build();
        Client client = Joystick.create(config);
        JoystickContent content = client.getContent(contentId);
        JsonNode jsonContent = content.asJson();

        return jsonContent.toString();
    }

    @PostMapping("/single/content/allParams")
    public String getContentByClientConfig(
        @RequestParam final String apiKey,
        @RequestParam final String contentId,
        @RequestBody(required = false) final ClientConfigDto clientConfigDto) throws JsonProcessingException {
        ClientConfig clientConfig = ClientConfig.builder().setApiKey(apiKey)
            .setUserId(clientConfigDto.getUserId())
            .setParams(clientConfigDto.getParams())
            .setSemVer(clientConfigDto.getSemVer())
            .setSerialized(clientConfigDto.isSerialized())
            .build();
        Client client = Joystick.create(clientConfig);
        JoystickContent content = client.getContent(contentId);
        JsonNode jsonContent = content.asObject(JsonNode.class);

        return jsonContent.toString();
    }

    @PostMapping("/single/fullContent/allParams")
    public String getFullContentByClientConfig(
        @RequestParam final String apiKey,
        @RequestParam final String contentId,
        @RequestBody(required = false) final ClientConfigDto clientConfigDto) {
        ClientConfig clientConfig = ClientConfig.builder().setApiKey(apiKey)
            .setUserId(clientConfigDto.getUserId())
            .setParams(clientConfigDto.getParams())
            .setSemVer(clientConfigDto.getSemVer())
            .setSerialized(clientConfigDto.isSerialized())
            .build();
        Client client = Joystick.create(clientConfig);
        JoystickFullContent<JoystickContent> jsonContent = client.getFullContent(contentId);

        return jsonContent.toString();
    }

}
