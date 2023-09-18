package com.getjoystick.sdk.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.Collections;
import java.util.Map;

@Data
@Builder(setterPrefix = "set")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestBody {

    /**
     * Value of the user ID. Otherwise, an empty string.
     */
    @Builder.Default
    private String u = "";

    /**
     * should always be the key-value object. If params are not set at the client config level – pass empty object
     * (in the JSON encoded version it will look like: {} )
     */
    @Builder.Default
    private Map<Object, Object> p = Collections.emptyMap();

    /**
     * should be present only if the semantical version was provided in the client configuration.
     * If it’s not – the v field should be omitted in the request body
     */
    private String v;

}
