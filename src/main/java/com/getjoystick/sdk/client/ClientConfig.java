package com.getjoystick.sdk.client;

import com.getjoystick.sdk.cache.ApiCache;
import com.getjoystick.sdk.cache.impl.ApiCacheLRU;
import com.getjoystick.sdk.errors.ConfigurationException;
import java.util.Collections;
import java.util.Map;
import java.util.regex.Pattern;

public class ClientConfig {
    private String userId;
    private String apiKey;
    private String semVer;
    private Map<Object, Object> params;
    private int cacheTTL;
    private boolean serialized;
    private ApiCache<String, String> cache;

    public static ClientConfigBuilder builder() {
        return new OverridedClientConfigBuilder();
    }

    private static Map<Object, Object> defaultParams() {
        return Collections.emptyMap();
    }

    private static ApiCache<String, String> defaultCache() {
        return new ApiCacheLRU<>();
    }

    /* default */ ClientConfig(final String userId, final String apiKey, final String semVer,
                               final Map<Object, Object> params, final int cacheTTL,
                               final boolean serialized, final ApiCache<String, String> cache) {
        this.userId = userId;
        this.apiKey = apiKey;
        this.semVer = semVer;
        this.params = params;
        this.cacheTTL = cacheTTL;
        this.serialized = serialized;
        this.cache = cache;
    }

    public String getUserId() {
        return this.userId;
    }

    public String getApiKey() {
        return this.apiKey;
    }

    public String getSemVer() {
        return this.semVer;
    }

    public Map<Object, Object> getParams() {
        return this.params;
    }

    public int getCacheTTL() {
        return this.cacheTTL;
    }

    public boolean isSerialized() {
        return this.serialized;
    }

    public ApiCache<String, String> getCache() {
        return this.cache;
    }

    @Override
    public boolean equals(final Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof ClientConfig)) {
            return false;
        }

        final ClientConfig other = (ClientConfig)object;

        final Object thisApiKey = this.getApiKey();
        final Object otherApiKey = other.getApiKey();
        if (thisApiKey == null) {
            if (otherApiKey != null) {
                return false;
            }
        } else if (!thisApiKey.equals(otherApiKey)) {
            return false;
        }

        if (this.getCacheTTL() != other.getCacheTTL()) {
            return false;
        }
        if (this.isSerialized() != other.isSerialized()) {
            return false;
        }

        final Object thisUserId = this.getUserId();
        final Object otherUserId = other.getUserId();
        if (thisUserId == null) {
            if (otherUserId != null) {
                return  false;
            }
        } else if (!thisUserId.equals(otherUserId)) {
            return false;
        }

        final Object thisSemVer = this.getSemVer();
        final Object otherSemVer = other.getSemVer();
        if (thisSemVer == null) {
            if (otherSemVer != null) {
                return false;
            }
        } else if (!thisSemVer.equals(otherSemVer)) {
            return false;
        }

        final Object thisParams = this.getParams();
        final Object otherParams = other.getParams();
        if (thisParams == null) {
            if (otherParams != null) {
                return false;
            }
        } else if (!thisParams.equals(otherParams)) {
            return false;
        }

        final Object thisCache = this.getCache();
        final Object otherCache = other.getCache();
        if (thisCache == null) {
            return otherCache == null;
        }
        return thisCache.equals(otherCache);
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = result * 59 + this.getCacheTTL();
        result = result * 59 + (this.isSerialized() ? 79 : 97);
        final Object userIdObj = this.getUserId();
        result = result * 59 + (userIdObj == null ? 43 : userIdObj.hashCode());
        final Object apiKeyObj = this.getApiKey();
        result = result * 59 + (apiKeyObj == null ? 43 : apiKeyObj.hashCode());
        final Object semVerObj = this.getSemVer();
        result = result * 59 + (semVerObj == null ? 43 : semVerObj.hashCode());
        final Object paramsObj = this.getParams();
        result = result * 59 + (paramsObj == null ? 43 : paramsObj.hashCode());
        final Object cacheObj = this.getCache();
        result = result * 59 + (cacheObj == null ? 43 : cacheObj.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "ClientConfig(userId=" + this.getUserId() + ", apiKey=" + this.getApiKey() + ", semVer=" + this.getSemVer() + ", params=" + this.getParams() + ", cacheTTL=" + this.getCacheTTL() + ", serialized=" + this.isSerialized() + ", cache=" + this.getCache() + ")";
    }

    public static class ClientConfigBuilder {
        private boolean userIdSet;
        private String userIdValue;
        private String apiKey;
        private String semVer;
        private boolean paramsSet;
        private Map<Object, Object> paramsValue;
        private int cacheTTL;
        private boolean serialized;
        private boolean cacheSet;
        private ApiCache<String, String> cacheValue;

        /* default */ ClientConfigBuilder() {
        }

        public ClientConfigBuilder setUserId(final String userId) {
            this.userIdValue = userId;
            this.userIdSet = true;
            return this;
        }

        public ClientConfigBuilder setApiKey(final String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public ClientConfigBuilder setSemVer(final String semVer) {
            this.semVer = semVer;
            return this;
        }

        public ClientConfigBuilder setParams(final Map<Object, Object> params) {
            this.paramsValue = params;
            this.paramsSet = true;
            return this;
        }

        public ClientConfigBuilder setCacheTTL(final int cacheTTL) {
            this.cacheTTL = cacheTTL;
            return this;
        }

        public ClientConfigBuilder setSerialized(final boolean serialized) {
            this.serialized = serialized;
            return this;
        }

        public ClientConfigBuilder setCache(final ApiCache<String, String> cache) {
            this.cacheValue = cache;
            this.cacheSet = true;
            return this;
        }

        public ClientConfig build() {
            String thisUserIdValue = this.userIdValue;
            if (!this.userIdSet) {
                thisUserIdValue = "";
            }

            Map<Object, Object> thisParamsValue = this.paramsValue;
            if (!this.paramsSet) {
                thisParamsValue = ClientConfig.defaultParams();
            }

            ApiCache<String, String> thisCacheValue = this.cacheValue;
            if (!this.cacheSet) {
                thisCacheValue = ClientConfig.defaultCache();
            }

            return new ClientConfig(thisUserIdValue, this.apiKey, this.semVer, thisParamsValue, this.cacheTTL, this.serialized, thisCacheValue);
        }

        @Override
        public String toString() {
            return "ClientConfig.ClientConfigBuilder(userId$value=" + this.userIdValue + ", apiKey=" + this.apiKey + ", semVer=" + this.semVer + ", params$value=" + this.paramsValue + ", cacheTTL=" + this.cacheTTL + ", serialized=" + this.serialized + ", cache$value=" + this.cacheValue + ")";
        }
    }

    private static class OverridedClientConfigBuilder extends ClientConfigBuilder {
        private static final String SEM_VER_REGEXP = "^(0|[1-9]\\d*)\\.(0|[1-9]\\d*)\\.(0|[1-9]\\d*)$";
        private static final Pattern SEM_VER_PATTERN = Pattern.compile(SEM_VER_REGEXP);

        private OverridedClientConfigBuilder() {
            super();
        }

        private void validateSemVersion(final String version) {
            if (version != null && !SEM_VER_PATTERN.matcher(version).matches()) {
                throw new ConfigurationException("The semantic version [" + version + "] is incorrect.");
            }
        }

        private void validateCacheTTL(final int seconds) {
            if (seconds < 0) {
                throw new ConfigurationException("Cache expiration time must be defined as seconds and must be positive.");
            }
        }

        @Override
        public ClientConfigBuilder setSemVer(final String semVer) {
            this.validateSemVersion(semVer);
            return super.setSemVer(semVer);
        }

        @Override
        public ClientConfigBuilder setCacheTTL(final int cacheTTL) {
            this.validateCacheTTL(cacheTTL);
            return super.setCacheTTL(cacheTTL);
        }
        @Override
        public ClientConfig build() {
            if (super.apiKey != null && !super.apiKey.trim().isEmpty()) {
                return super.build();
            } else {
                throw new ConfigurationException("API key is not provided.");
            }
        }
    }
}
