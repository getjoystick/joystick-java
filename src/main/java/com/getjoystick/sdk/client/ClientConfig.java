package com.getjoystick.sdk.client;

import com.getjoystick.sdk.cache.ApiCache;
import com.getjoystick.sdk.cache.impl.ApiCacheLRU;
import com.getjoystick.sdk.errors.ConfigurationException;
import lombok.Builder;
import lombok.Data;

import java.util.Collections;
import java.util.Map;
import java.util.regex.Pattern;

@Data
@Builder(setterPrefix = "set")
public class ClientConfig {

    @Builder.Default
    private String userId = "";
    private String apiKey;
    private String semVer;
    @Builder.Default
    private Map<Object, Object> params = Collections.emptyMap();
    private int cacheTTL;
    private boolean serialized;
    @Builder.Default
    private ApiCache<String, String> cache = new ApiCacheLRU<>();

    /**
     * Override standard builder with custom implementation
     */
    public static ClientConfigBuilder builder() {
        return new OverridedClientConfigBuilder();
    }

    /**
     * Add additional validations as part of the build() method
     */
    private static class OverridedClientConfigBuilder extends ClientConfigBuilder {

        /**
         * Semantic version validation from
         * <a href="https://semver.org/#is-there-a-suggested-regular-expression-regex-to-check-a-semver-string">standard validation</a>
         * without pre-release data.
         */
        private static final String SEM_VER_REGEXP = "^(0|[1-9]\\d*)\\.(0|[1-9]\\d*)\\.(0|[1-9]\\d*)$";
        private static final Pattern SEM_VER_PATTERN = Pattern.compile(SEM_VER_REGEXP);

        /**
         * Fast-fail semantic version validation
         *
         * @throws ConfigurationException in case of invalid semantic version
         */
        private void validateSemVersion(final String version) {
            if (version != null && !SEM_VER_PATTERN.matcher(version).matches()) {
                throw new ConfigurationException("The semantic version [" + version + "] is incorrect.");
            }
        }

        /**
         * Fast-fail cache expiration value validation
         *
         * @throws ConfigurationException in case of invalid value
         */
        private void validateCacheTTL(final int seconds) {
            if (seconds < 0) {
                throw new ConfigurationException("Cache expiration time must be defined as seconds and must be positive.");
            }
        }

        /**
         * @param semVer is a semantic version of your app that is making the request. Format example: '1.0.0'
         * @return this {@link  ClientConfigBuilder} instance
         */
        @Override
        public ClientConfigBuilder setSemVer(final String semVer) {
            validateSemVersion(semVer);
            return super.setSemVer(semVer);
        }

        /**
         * @param cacheTTL is an expiration time in seconds to invalidate the cache
         * @return this {@link  ClientConfigBuilder} instance
         */
        @Override
        public ClientConfigBuilder setCacheTTL(final int cacheTTL) {
            validateCacheTTL(cacheTTL);
            return super.setCacheTTL(cacheTTL);
        }

        @Override
        public ClientConfig build() {
            if (super.apiKey == null || super.apiKey.trim().isEmpty()) {
                throw new ConfigurationException("API key is not provided.");
            }
            return super.build();
        }

    }

}
