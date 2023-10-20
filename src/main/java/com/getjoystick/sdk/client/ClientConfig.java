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

    ClientConfig(String userId, String apiKey, String semVer, Map<Object, Object> params, int cacheTTL, boolean serialized, ApiCache<String, String> cache) {
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

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public void setSemVer(String semVer) {
        this.semVer = semVer;
    }

    public void setParams(Map<Object, Object> params) {
        this.params = params;
    }

    public void setCacheTTL(int cacheTTL) {
        this.cacheTTL = cacheTTL;
    }

    public void setSerialized(boolean serialized) {
        this.serialized = serialized;
    }

    public void setCache(ApiCache<String, String> cache) {
        this.cache = cache;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof ClientConfig)) {
            return false;
        } else {
            ClientConfig other = (ClientConfig)o;
            if (this.getCacheTTL() != other.getCacheTTL()) {
                return false;
            } else if (this.isSerialized() != other.isSerialized()) {
                return false;
            } else {
                Object thisUserId = this.getUserId();
                Object otherUserId = other.getUserId();
                if (thisUserId == null) {
                    if (otherUserId != null) {
                        return  false;
                    }
                } else if (!thisUserId.equals(otherUserId)) {
                    return false;
                }

                Object thisApiKey = this.getApiKey();
                Object otherApiKey = other.getApiKey();
                if (thisApiKey == null) {
                    if (otherApiKey != null) {
                        return false;
                    }
                } else if (!thisApiKey.equals(otherApiKey)) {
                    return false;
                }

                Object thisSemVer = this.getSemVer();
                Object otherSemVer = other.getSemVer();
                if (thisSemVer == null) {
                    if (otherSemVer != null) {
                        return false;
                    }
                } else if (!thisSemVer.equals(otherSemVer)) {
                    return false;
                }

                Object thisParams = this.getParams();
                Object otherParams = other.getParams();
                if (thisParams == null) {
                    if (otherParams != null) {
                        return false;
                    }
                } else if (!thisParams.equals(otherParams)) {
                    return false;
                }

                Object thisCache = this.getCache();
                Object otherCache = other.getCache();
                if (thisCache == null) {
                    return otherCache == null;
                } else return thisCache.equals(otherCache);
            }
        }
    }

    public int hashCode() {
        int result = 1;
        result = result * 59 + this.getCacheTTL();
        result = result * 59 + (this.isSerialized() ? 79 : 97);
        Object userIdObj = this.getUserId();
        result = result * 59 + (userIdObj == null ? 43 : userIdObj.hashCode());
        Object apiKeyObj = this.getApiKey();
        result = result * 59 + (apiKeyObj == null ? 43 : apiKeyObj.hashCode());
        Object semVerObj = this.getSemVer();
        result = result * 59 + (semVerObj == null ? 43 : semVerObj.hashCode());
        Object paramsObj = this.getParams();
        result = result * 59 + (paramsObj == null ? 43 : paramsObj.hashCode());
        Object cacheObj = this.getCache();
        result = result * 59 + (cacheObj == null ? 43 : cacheObj.hashCode());
        return result;
    }

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

        ClientConfigBuilder() {
        }

        public ClientConfigBuilder setUserId(String userId) {
            this.userIdValue = userId;
            this.userIdSet = true;
            return this;
        }

        public ClientConfigBuilder setApiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public ClientConfigBuilder setSemVer(String semVer) {
            this.semVer = semVer;
            return this;
        }

        public ClientConfigBuilder setParams(Map<Object, Object> params) {
            this.paramsValue = params;
            this.paramsSet = true;
            return this;
        }

        public ClientConfigBuilder setCacheTTL(int cacheTTL) {
            this.cacheTTL = cacheTTL;
            return this;
        }

        public ClientConfigBuilder setSerialized(boolean serialized) {
            this.serialized = serialized;
            return this;
        }

        public ClientConfigBuilder setCache(ApiCache<String, String> cache) {
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

        public String toString() {
            return "ClientConfig.ClientConfigBuilder(userId$value=" + this.userIdValue + ", apiKey=" + this.apiKey + ", semVer=" + this.semVer + ", params$value=" + this.paramsValue + ", cacheTTL=" + this.cacheTTL + ", serialized=" + this.serialized + ", cache$value=" + this.cacheValue + ")";
        }
    }

    private static class OverridedClientConfigBuilder extends ClientConfigBuilder {
        private static final String SEM_VER_REGEXP = "^(0|[1-9]\\d*)\\.(0|[1-9]\\d*)\\.(0|[1-9]\\d*)$";
        private static final Pattern SEM_VER_PATTERN = Pattern.compile(SEM_VER_REGEXP);

        private OverridedClientConfigBuilder() {
        }

        private void validateSemVersion(String version) {
            if (version != null && !SEM_VER_PATTERN.matcher(version).matches()) {
                throw new ConfigurationException("The semantic version [" + version + "] is incorrect.");
            }
        }

        private void validateCacheTTL(int seconds) {
            if (seconds < 0) {
                throw new ConfigurationException("Cache expiration time must be defined as seconds and must be positive.");
            }
        }

        @Override
        public ClientConfigBuilder setSemVer(String semVer) {
            this.validateSemVersion(semVer);
            return super.setSemVer(semVer);
        }

        @Override
        public ClientConfigBuilder setCacheTTL(int cacheTTL) {
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
