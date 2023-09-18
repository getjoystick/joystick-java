package com.getjoystick.sdk.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.getjoystick.sdk.client.ClientConfig;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ApiCacheKeyUtilTest {

    private static final String TEST_API_KEY = "test-api-key";

    @Test
    void constructor_default_illegalStateForUtilityClass() {
        assertThrows(IllegalStateException.class, ApiCacheKeyUtil::new);
    }

    @Test
    void testIfGetHashProvideTheSameKeyForMultipleInvocations() {
        final String hash = "613bbacdc003c8ad5c1044a9f44527c0dd2c42129edf8def94f5b57002aebb37";
        final ClientConfig cfg = ClientConfig.builder()
            .setApiKey(TEST_API_KEY)
            .setParams(
                ImmutableMap.of(
                    "p1", "v1",
                    "p2", "v2",
                    "p3", "v3"
                )
            )
            .setSemVer("1.0.0")
            .setUserId("user-id-1")
            .build();
        assertEquals(
            hash,
            ApiCacheKeyUtil.getHash(cfg, ImmutableList.of("id1", "id2"), true, false)
        );
        assertEquals(
            hash,
            ApiCacheKeyUtil.getHash(cfg, ImmutableList.of("id1", "id2"), true, false)
        );
    }

    @Test
    void testGetHashFromSameConfigurationsButDifferentObjects() {
        final ClientConfig cfg1 = ClientConfig.builder()
            .setApiKey(TEST_API_KEY)
            .setParams(
                ImmutableMap.of(
                    "p1", "v1",
                    "p2", "v2",
                    "p3", "v3"
                )
            )
            .setSemVer("1.0.0")
            .setUserId("user-id-1")
            .build();
        final ClientConfig cfg2 = ClientConfig.builder()
            .setApiKey(TEST_API_KEY)
            .setParams(
                ImmutableMap.of(
                    "p1", "v1",
                    "p2", "v2",
                    "p3", "v3"
                )
            )
            .setSemVer("1.0.0")
            .setUserId("user-id-1")
            .build();
        final String hash1 = ApiCacheKeyUtil.getHash(cfg1, ImmutableList.of("id1", "id2"), true, false);
        final String hash2 = ApiCacheKeyUtil.getHash(cfg2, ImmutableList.of("id1", "id2"), true, false);
        assertEquals(hash1, hash2);
    }

    @Test
    void testParamsOrdering() {
        final ClientConfig cfg1 = ClientConfig.builder()
            .setApiKey(TEST_API_KEY)
            .setParams(
                ImmutableMap.of(
                    "p1", "v1",
                    "p2", "v2",
                    "p3", "v3"
                )
            )
            .setSemVer("1.0.0")
            .setUserId("user-id-1")
            .build();
        final ClientConfig cfg2 = ClientConfig.builder()
            .setApiKey(TEST_API_KEY)
            .setParams(
                ImmutableMap.of(
                    "p3", "v3",
                    "p2", "v2",
                    "p1", "v1"
                )
            )
            .setSemVer("1.0.0")
            .setUserId("user-id-1")
            .build();
        final String hash1 = ApiCacheKeyUtil.getHash(cfg1, ImmutableList.of("id1", "id2"), true, false);
        final String hash2 = ApiCacheKeyUtil.getHash(cfg2, ImmutableList.of("id1", "id2"), true, false);
        assertEquals(hash1, hash2);
    }

    @Test
    void testContentsIdsOrdering() {
        final ClientConfig cfg1 = ClientConfig.builder()
            .setApiKey(TEST_API_KEY)
            .setParams(
                ImmutableMap.of(
                    "p1", "v1",
                    "p2", "v2",
                    "p3", "v3"
                )
            )
            .setSemVer("1.0.0")
            .setUserId("user-id-1")
            .build();
        final ClientConfig cfg2 = ClientConfig.builder()
            .setApiKey(TEST_API_KEY)
            .setParams(
                ImmutableMap.of(
                    "p3", "v3",
                    "p2", "v2",
                    "p1", "v1"
                )
            )
            .setSemVer("1.0.0")
            .setUserId("user-id-1")
            .build();
        final String hash1 = ApiCacheKeyUtil.getHash(cfg1, ImmutableList.of("id1", "id2", "id3"), true, false);
        final String hash2 = ApiCacheKeyUtil.getHash(cfg2, ImmutableList.of("id3", "id2", "id1"), true, false);
        assertEquals(hash1, hash2);
    }

}
