package com.getjoystick.sdk.cache;

/**
 * Interface for cache used in Joystick configuration
 *
 * @param <K> Cache key type
 * @param <V> Cache value type
 */
public interface ApiCache<K, V> {

    /**
     * Get item from cache by key
     *
     * @param key Key of cached item
     * @return Cached value
     */
    V get(K key);

    /**
     * Put item to cache
     *
     * @param key Key of cached item
     * @param value Value of cached item
     */
    void put(K key, V value);

}
