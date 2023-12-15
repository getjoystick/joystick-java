package com.getjoystick.sdk.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collections;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestBody {

    /**
     * Value of the user ID. Otherwise, an empty string.
     */
    @JsonProperty("u")
    private String userId;

    /**
     * should always be the key-value object. If params are not set at the client config level – pass empty object
     * (in the JSON encoded version it will look like: {} )
     */
    @JsonProperty("p")
    private Map<Object, Object> parameters;

    /**
     * should be present only if the semantical version was provided in the client configuration.
     * If it’s not – the v field should be omitted in the request body
     */
    @JsonProperty("v")
    private String semanticVersion;

    private static Map<Object, Object> defaultParameters() {
        return Collections.emptyMap();
    }

    /* default */ RequestBody(final String userId, final Map<Object, Object> parameters, final String semanticVersion) {
        this.userId = userId;
        this.parameters = parameters;
        this.semanticVersion = semanticVersion;
    }

    public static RequestBodyBuilder builder() {
        return new RequestBodyBuilder();
    }

    public String getUserId() {
        return this.userId == null ? "" : this.userId;
    }

    public Map<Object, Object> getParameters() {
        return this.parameters == null ? defaultParameters() : this.parameters;
    }

    public String getSemanticVersion() {
        return this.semanticVersion;
    }

    @JsonProperty("u")
    public void setUserId(final String userId) {
        this.userId = userId;
    }

    @JsonProperty("p")
    public void setParameters(final Map<Object, Object> parameters) {
        this.parameters = parameters;
    }

    @JsonProperty("v")
    public void setSemanticVersion(final String semanticVersion) {
        this.semanticVersion = semanticVersion;
    }

    @Override
    public boolean equals(final Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof RequestBody)) {
            return false;
        }
        final RequestBody other = (RequestBody)object;

        final Object thisUserId = this.getUserId();
        final Object otherUserId = other.getUserId();
        if (thisUserId == null) {
            if (otherUserId != null) {
                return false;
            }
        } else if (!thisUserId.equals(otherUserId)) {
            return false;
        }

        final Object thisParameters = this.getParameters();
        final Object otherParameters = other.getParameters();
        if (thisParameters == null) {
            if (otherParameters != null) {
                return false;
            }
        } else if (!thisParameters.equals(otherParameters)) {
            return false;
        }

        final Object thisSemVersion = this.getSemanticVersion();
        final Object otherSemVersion = other.getSemanticVersion();
        if (thisSemVersion == null) {
            return otherSemVersion == null;
        }
        return thisSemVersion.equals(otherSemVersion);
    }

    @Override
    public int hashCode() {
        int result = 1;
        final Object thisUserId = this.getUserId();
        result = result * 59 + (thisUserId == null ? 43 : thisUserId.hashCode());
        final Object thisParameters = this.getParameters();
        result = result * 59 + (thisParameters == null ? 43 : thisParameters.hashCode());
        final Object thisSemVersion = this.getSemanticVersion();
        result = result * 59 + (thisSemVersion == null ? 43 : thisSemVersion.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "RequestBody(userId=" + this.getUserId() + ", parameters=" + this.getParameters() + ", semanticVersion=" + this.getSemanticVersion() + ")";
    }

    public static class RequestBodyBuilder {
        private boolean userIdSet;
        private String userIdValue;
        private boolean parametersSet;
        private Map<Object, Object> parametersValue;
        private String semanticVersion;

        /* default */ RequestBodyBuilder() {
        }

        @JsonProperty("u")
        public RequestBodyBuilder setUserId(final String userId) {
            this.userIdValue = userId;
            this.userIdSet = true;
            return this;
        }

        @JsonProperty("p")
        public RequestBodyBuilder setParameters(final Map<Object, Object> parameters) {
            this.parametersValue = parameters;
            this.parametersSet = true;
            return this;
        }

        @JsonProperty("v")
        public RequestBodyBuilder setSemanticVersion(final String semanticVersion) {
            this.semanticVersion = semanticVersion;
            return this;
        }

        public RequestBody build() {
            String thisUserIdValue = this.userIdValue;
            if (!this.userIdSet) {
                thisUserIdValue = "";
            }

            Map<Object, Object> thisParamsValue = this.parametersValue;
            if (!this.parametersSet) {
                thisParamsValue = RequestBody.defaultParameters();
            }

            return new RequestBody(thisUserIdValue, thisParamsValue, this.semanticVersion);
        }

        @Override
        public String toString() {
            return "RequestBody.RequestBodyBuilder(userId$value=" + this.userIdValue + ", parameters$value=" + this.parametersValue + ", semanticVersion=" + this.semanticVersion + ")";
        }
    }

}
