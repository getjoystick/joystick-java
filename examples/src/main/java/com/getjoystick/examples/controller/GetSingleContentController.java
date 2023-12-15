package com.getjoystick.examples.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.getjoystick.examples.model.ClientConfigDto;
import com.getjoystick.sdk.Joystick;
import com.getjoystick.sdk.client.Client;
import com.getjoystick.sdk.client.ClientConfig;
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
        return client.getContent(contentId).toString();
    }

    @PostMapping("/single/content/allParams")
    public String getContentByClientConfig(
        @RequestParam final String apiKey,
        @RequestParam final String contentId,
        @RequestBody(required = false) final ClientConfigDto clientConfigDto) {
        ClientConfig clientConfig = ClientConfig.builder().setApiKey(apiKey)
            .setUserId(clientConfigDto.getUserId())
            .setParams(clientConfigDto.getParams())
            .setSemVer(clientConfigDto.getSemVer())
            .build();
        Client client = Joystick.create(clientConfig);

        return client.getContent(contentId).toString();
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
            .build();
        Client client = Joystick.create(clientConfig);
        JoystickFullContent<JsonNode> jsonContent = client.getFullContent(contentId);

        return jsonContent.toString();
    }

}
