package com.getjoystick.sdk.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.getjoystick.sdk.errors.ConfigurationException;
import java.util.Arrays;

@JsonInclude(Include.NON_NULL)
public class PublishData {
    @JsonProperty("d")
    private String description;
    @JsonProperty("c")
    private Object content;
    @JsonProperty("m")
    private Object[] dynamicContentMap;

    private static Object[] defaultDynamicContentMap() {
        return new Object[0];
    }

    public PublishData(final String description, final Object content, final Object[] dynamicContentMap) {
        this.description = description;
        this.content = content;
        this.dynamicContentMap = dynamicContentMap;
    }

    public static PublishDataBuilder builder() {
        return new PublishDataBuilder();
    }

    public String getDescription() {
        return this.description;
    }

    public Object getContent() {
        return this.content;
    }

    public Object[] getDynamicContentMap() {
        return this.dynamicContentMap;
    }

    @JsonProperty("d")
    public void setDescription(final String description) {
        this.description = description;
    }

    @JsonProperty("c")
    public void setContent(final Object content) {
        this.content = content;
    }

    @JsonProperty("m")
    public void setDynamicContentMap(final Object[] dynamicContentMap) {
        this.dynamicContentMap = dynamicContentMap;
    }

    @Override
    public boolean equals(final Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof PublishData)) {
            return false;
        }
        final PublishData other = (PublishData)object;
        final Object thisDescription = this.getDescription();
        final Object otherDescription = other.getDescription();
        if (thisDescription == null) {
            if (otherDescription != null) {
                return false;
            }
        } else if (!thisDescription.equals(otherDescription)) {
            return false;
        }

        final Object thisContent = this.getContent();
        final Object otherContent = other.getContent();
        if (thisContent == null) {
            if (otherContent != null) {
                return false;
            }
        } else if (!thisContent.equals(otherContent)) {
            return false;
        }

        return Arrays.deepEquals(this.getDynamicContentMap(), other.getDynamicContentMap());
    }

    @Override
    public int hashCode() {
        int result = 1;
        final Object descriptionObj = this.getDescription();
        result = result * 59 + (descriptionObj == null ? 43 : descriptionObj.hashCode());
        final Object contentObj = this.getContent();
        result = result * 59 + (contentObj == null ? 43 : contentObj.hashCode());
        result = result * 59 + Arrays.deepHashCode(this.getDynamicContentMap());
        return result;
    }
    @Override
    public String toString() {
        return "PublishData(description=" + this.getDescription() + ", content=" + this.getContent() + ", dynamicContentMap=" + Arrays.deepToString(this.getDynamicContentMap()) + ")";
    }

    public static class PublishDataBuilder {
        private String description;
        private Object content;
        private boolean dynamicContentSet;
        private Object[] dynamicContentValue;

        /* default */ PublishDataBuilder() {
        }

        @JsonProperty("d")
        public PublishDataBuilder setDescription(final String description) {
            this.description = description;
            return this;
        }

        @JsonProperty("c")
        public PublishDataBuilder setContent(final Object content) {
            this.content = content;
            return this;
        }

        @JsonProperty("m")
        public PublishDataBuilder setDynamicContentMap(final Object[] dynamicContentMap) {
            this.dynamicContentValue = dynamicContentMap;
            this.dynamicContentSet = true;
            return this;
        }

        public PublishData build() {
            if (description == null) {
                throw new ConfigurationException("Description is not provided.");
            } else {
                final int length = description.trim().length();
                if (length >= 1 && length <= 50) {
                    if (content == null) {
                        throw new ConfigurationException("Content is not provided.");
                    }
                } else {
                    throw new ConfigurationException("There must be more than 0 and no more than 50 symbols in the description.");
                }
            }
            Object[] thisDynamicContVal = this.dynamicContentValue;
            if (!this.dynamicContentSet) {
                thisDynamicContVal = PublishData.defaultDynamicContentMap();
            }

            return new PublishData(this.description, this.content, thisDynamicContVal);
        }

        @Override
        public String toString() {
            return "PublishData.PublishDataBuilder(description=" + this.description + ", content=" + this.content + ", dynamicContentMap$value=" + Arrays.deepToString(this.dynamicContentValue) + ")";
        }
    }
}
