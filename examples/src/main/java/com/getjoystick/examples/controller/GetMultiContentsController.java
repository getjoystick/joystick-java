package com.getjoystick.examples.controller;

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

import java.util.List;
import java.util.Map;

@RestController
public class GetMultiContentsController {

    @GetMapping("/multi/contents")
    public String getContents(@RequestParam final String apiKey,
                              @RequestParam final List<String> contentIds) {
        ClientConfig config = ClientConfig.builder().setApiKey(apiKey).build();
        Client client = Joystick.create(config);
        Map<String, JoystickContent> contentsMap = client.getContents(contentIds);

        return contentsMap.toString();
    }

    @PostMapping("/multi/contents/allParams")
    public String getContentsByClientConfig(
        @RequestParam final String apiKey,
        @RequestParam final List<String> contentIds,
        @RequestBody(required = false) final ClientConfigDto clientConfigDto) {
        ClientConfig clientConfig = ClientConfig.builder().setApiKey(apiKey)
            .setUserId(clientConfigDto.getUserId())
            .setParams(clientConfigDto.getParams())
            .setSemVer(clientConfigDto.getSemVer())
            .setSerialized(clientConfigDto.isSerialized())
            .build();
        Client client = Joystick.create(clientConfig);
        Map<String, JoystickContent> contentsMap = client.getContents(contentIds);

        return contentsMap.toString();
    }

    @PostMapping("/multi/fullContents/allParams")
    public String getFullContentsByClientConfig(
        @RequestParam final String apiKey,
        @RequestParam final List<String> contentIds,
        @RequestBody(required = false) final ClientConfigDto clientConfigDto) {
        ClientConfig clientConfig = ClientConfig.builder().setApiKey(apiKey)
            .setUserId(clientConfigDto.getUserId())
            .setParams(clientConfigDto.getParams())
            .setSemVer(clientConfigDto.getSemVer())
            .setSerialized(clientConfigDto.isSerialized())
            .build();
        Client client = Joystick.create(clientConfig);
        Map<String, JoystickFullContent<JoystickContent>> contentsMap = client.getFullContents(contentIds);

        return contentsMap.toString();
    }


}
