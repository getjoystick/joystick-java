package com.getjoystick.sdk.cache.impl;

import com.getjoystick.sdk.cache.ApiCache;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.time.Duration;

/**
 * LRU in memory cache implementation. Used by default in Joystick configuration.
 * @param <K> Key type
 * @param <V> Value type
 */
public class ApiCacheLRU<K, V> implements ApiCache<K, V> {

    /**
     * Maximum cache size
     */
    private static final int DEFAULT_SIZE = 1000;

    /**
     * Default cache expiration time in seconds
     */
    private static final int DEFAULT_EXPIRATION = 300;

    /**
     * Field to store cache
     */
    private final Cache<K, V> cache;

    /**
     * Default cache constructor.
     * Initializes in memory LRU cache with default parameter.
     */
    public ApiCacheLRU() {
        this(DEFAULT_EXPIRATION);
    }

    /**
     * Default cache constructor.
     * Initializes in memory LRU cache with default parameter.
     */
    public ApiCacheLRU(final int cacheExpirationSeconds) {
        final int expirationSeconds = cacheExpirationSeconds >= 0 ? cacheExpirationSeconds : DEFAULT_EXPIRATION;
        cache = CacheBuilder.newBuilder()
            .maximumSize(DEFAULT_SIZE)
            .expireAfterAccess(Duration.ofSeconds(expirationSeconds))
            .build();
    }


    /**
     * Get item from cache by key
     *
     * @param key Key of cached item
     * @return Cached value
     */
    @Override
    public V get(final K key) {
        return cache.getIfPresent(key);
    }

    /**
     * Put item to cache
     *
     * @param key Key of cached item
     * @param value Value of cached item
     */
    @Override
    public void put(final K key, final V value) {
        cache.put(key, value);
    }

}
