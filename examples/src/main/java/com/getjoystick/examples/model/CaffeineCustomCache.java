package com.getjoystick.examples.model;

import com.getjoystick.sdk.cache.ApiCache;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.concurrent.TimeUnit;

/**
 * Custom cache based on Caffeine
 */
public class CaffeineCustomCache<K, V> implements ApiCache<K, V> {

    private final Cache<K, V> cache;

    public CaffeineCustomCache() {
        cache = Caffeine.newBuilder()
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .maximumSize(100)
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
     * @param key   Key of cached item
     * @param value Value of cached item
     */
    @Override
    public void put(final K key, final V value) {
        cache.put(key, value);
    }

}
