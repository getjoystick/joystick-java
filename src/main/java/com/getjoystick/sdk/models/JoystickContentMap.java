package com.getjoystick.sdk.models;

import lombok.Data;

import java.util.Map;

@Data
public class JoystickContentMap {

    private Map<String, JoystickFullContentJson> contentMap;

}
