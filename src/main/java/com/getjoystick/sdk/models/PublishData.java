package com.getjoystick.sdk.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.getjoystick.sdk.errors.ConfigurationException;
import java.util.Arrays;

@JsonInclude(Include.NON_NULL)
public class PublishData {
    @JsonProperty("d")
    String description;
    @JsonProperty("c")
    Object content;
    @JsonProperty("m")
    Object[] dynamicContentMap;

    private static Object[] defaultDynamicContentMap() {
        return new Object[0];
    }

    PublishData(String description, Object content, Object[] dynamicContentMap) {
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
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("c")
    public void setContent(Object content) {
        this.content = content;
    }

    @JsonProperty("m")
    public void setDynamicContentMap(Object[] dynamicContentMap) {
        this.dynamicContentMap = dynamicContentMap;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof PublishData)) {
            return false;
        } else {
            PublishData other = (PublishData)o;
            Object thisDescription = this.getDescription();
            Object otherDescription = other.getDescription();
            if (thisDescription == null) {
                if (otherDescription != null) {
                    return false;
                }
            } else if (!thisDescription.equals(otherDescription)) {
                return false;
            }

            Object thisContent = this.getContent();
            Object otherContent = other.getContent();
            if (thisContent == null) {
                if (otherContent != null) {
                    return false;
                }
            } else if (!thisContent.equals(otherContent)) {
                return false;
            }

            return Arrays.deepEquals(this.getDynamicContentMap(), other.getDynamicContentMap());
        }
    }

    public int hashCode() {
        int result = 1;
        Object descriptionObj = this.getDescription();
        result = result * 59 + (descriptionObj == null ? 43 : descriptionObj.hashCode());
        Object contentObj = this.getContent();
        result = result * 59 + (contentObj == null ? 43 : contentObj.hashCode());
        result = result * 59 + Arrays.deepHashCode(this.getDynamicContentMap());
        return result;
    }

    public String toString() {
        return "PublishData(description=" + this.getDescription() + ", content=" + this.getContent() + ", dynamicContentMap=" + Arrays.deepToString(this.getDynamicContentMap()) + ")";
    }

    public static class PublishDataBuilder {
        private String description;
        private Object content;
        private boolean dynamicContentMapSet;
        private Object[] dynamicContentMapValue;

        PublishDataBuilder() {
        }

        @JsonProperty("d")
        public PublishDataBuilder setDescription(String description) {
            this.description = description;
            return this;
        }

        @JsonProperty("c")
        public PublishDataBuilder setContent(Object content) {
            this.content = content;
            return this;
        }

        @JsonProperty("m")
        public PublishDataBuilder setDynamicContentMap(Object[] dynamicContentMap) {
            this.dynamicContentMapValue = dynamicContentMap;
            this.dynamicContentMapSet = true;
            return this;
        }

        public PublishData build() {
            if (description == null) {
                throw new ConfigurationException("Description is not provided.");
            } else {
                int length = description.trim().length();
                if (length >= 1 && length <= 50) {
                    if (content == null) {
                        throw new ConfigurationException("Content is not provided.");
                    }
                } else {
                    throw new ConfigurationException("There must be more than 0 and no more than 50 symbols in the description.");
                }
            }
            Object[] thisDynamicContentMapValue = this.dynamicContentMapValue;
            if (!this.dynamicContentMapSet) {
                thisDynamicContentMapValue = PublishData.defaultDynamicContentMap();
            }

            return new PublishData(this.description, this.content, thisDynamicContentMapValue);
        }

        public String toString() {
            return "PublishData.PublishDataBuilder(description=" + this.description + ", content=" + this.content + ", dynamicContentMap$value=" + Arrays.deepToString(this.dynamicContentMapValue) + ")";
        }
    }
}
