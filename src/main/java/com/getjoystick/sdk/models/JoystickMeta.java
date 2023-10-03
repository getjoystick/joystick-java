package com.getjoystick.sdk.models;

import lombok.Data;

import java.util.List;

@Data
public class JoystickMeta {
    Long uid;

    Long mod;

    List<Object> variants;

    List<Object> seg;

}
