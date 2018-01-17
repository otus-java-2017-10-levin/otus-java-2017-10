package ru.otus.jsr107;

import org.junit.jupiter.api.Test;

import javax.cache.CacheManager;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

class MyCachingProviderTest {

    private  MyCachingProvider provider = new MyCachingProvider();
    @Test
    void getCacheManager() {
        CacheManager cache = provider.getCacheManager(null, null, null);
        CacheManager cache1 = provider.getCacheManager(null, null, null);
        assertEquals(true, cache != null);
        assertEquals(cache, cache1);
    }

    @Test
    void getDefaultClassLoader() {
    }

    @Test
    void getDefaultURI() {
    }

    @Test
    void getDefaultProperties() {
    }

    @Test
    void getCacheManager1() {
    }

    @Test
    void getCacheManager2() {
    }

    @Test
    void close() {
    }

    @Test
    void close1() {
    }

    @Test
    void close2() {
    }

}