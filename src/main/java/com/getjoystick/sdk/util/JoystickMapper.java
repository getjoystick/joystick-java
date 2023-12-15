package com.getjoystick.sdk.util;

import com.fasterxml.jackson.core.TreeNode;

public class JoystickMapper {

    /* default */ JoystickMapper() {
        throw new IllegalStateException("Utility class.");
    }

    public static  <T> T toObject(final TreeNode node, final Class<T> clazz) {
        return  JoystickUtil.treeToValue(node, clazz);
    }

}
