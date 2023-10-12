package com.getjoystick.sdk.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.getjoystick.sdk.errors.ConfigurationException;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(setterPrefix = "set")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PublishData {

    /**
     * Description of the content
     */
    @JsonProperty(value = "d")
    String description;
    /**
     * Content which can be encoded to JSON (including strings, numbers, boolean, maps and arrays)
     */
    @JsonProperty(value = "c")
    Object content;

    @Builder.Default
    @JsonProperty(value = "m")
    Object[] dynamicContentMap = new Object[0];

    public static PublishData.PublishDataBuilder builder() {
        return new PublishData.OverridePublishDataBuilder();
    }

    private static class OverridePublishDataBuilder extends
        PublishData.PublishDataBuilder {
        @Override
        public PublishData build() {
            if (super.description == null) {
                throw new ConfigurationException("Description is not provided.");
            }
            final int length = super.description.trim().length();
            if (length < 1 || length > 50) {
                throw new ConfigurationException(
                    "There must be more than 0 and no more than 50 symbols in the description."
                );
            }
            if (super.content == null) {
                throw new ConfigurationException("Content is not provided.");
            }
            return super.build();
        }
    }
}
