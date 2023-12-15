package com.getjoystick.sdk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class BaseTest {

    protected String toString(String path) throws IOException {
        try (final InputStream is = this.getClass().getResourceAsStream(path)) {
            return new BufferedReader(
                new InputStreamReader(Objects.requireNonNull(is), StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining(""));
        }
    }

}
