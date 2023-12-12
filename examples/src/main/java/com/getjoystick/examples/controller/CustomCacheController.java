package com.getjoystick.examples.controller;

import com.getjoystick.examples.model.CaffeineCustomCache;
import com.getjoystick.sdk.Joystick;
import com.getjoystick.sdk.client.Client;
import com.getjoystick.sdk.client.ClientConfig;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller contains examples how to use custom cache implementation.
 */
@RestController
public class CustomCacheController {

    @GetMapping("/cached/content")
    public String getContent(@RequestParam final String apiKey,
                             @RequestParam final String contentId) {
        ClientConfig config = ClientConfig.builder()
            .setApiKey(apiKey)
            .setCache(new CaffeineCustomCache<>())
            .build();
        Client client = Joystick.create(config);
        return client.getContent(contentId).toString();
    }

}
