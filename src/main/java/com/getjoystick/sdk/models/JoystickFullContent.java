package com.getjoystick.sdk.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JoystickFullContent<T> {

    private T data;

    private JoystickMeta meta;

    private String hash;
}
