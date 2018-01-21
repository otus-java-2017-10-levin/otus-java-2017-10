package ru.otus.jsr107;

import org.junit.jupiter.api.Test;

import javax.cache.CacheException;
import javax.cache.CacheManager;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.AccessedExpiryPolicy;
import javax.cache.expiry.Duration;

import static org.junit.jupiter.api.Assertions.*;

class MyCacheManagerTest {

    private final CacheManager manager = new MyCachingProvider().getCacheManager();
    private final MutableConfiguration<String, Integer> config =
            new MutableConfiguration<String, Integer>()
                    .setTypes(String.class, Integer.class)
                    .setExpiryPolicyFactory(AccessedExpiryPolicy.factoryOf(Duration.ONE_HOUR))
                    .setStatisticsEnabled(true);

    @Test
    void createCache() {
        assertThrows(NullPointerException.class, () ->manager.createCache("ints", null));
        assertThrows(NullPointerException.class, () ->manager.createCache(null, config));
    }

    @Test
    void createCache1() {
        manager.createCache("ints", config);
        assertThrows(CacheException.class, () ->manager.createCache("ints", config));
    }

    @Test
    void createCacheOnClosedManager() {
        manager.close();
        assertThrows(IllegalStateException.class, () ->manager.createCache("ints", config));
    }

    @Test
    void getCache() {
        assertEquals(null, manager.getCache("ints"));
        manager.close();
        assertThrows(IllegalStateException.class, () ->manager.getCache("ints"));
    }

    @Test
    void getCache1() {
        assertThrows(NullPointerException.class, () ->manager.getCache(null));
    }
}