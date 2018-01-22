package ru.otus.jsr107;

import javax.cache.Cache;
import javax.cache.CacheException;
import javax.cache.CacheManager;
import javax.cache.configuration.Configuration;
import javax.cache.spi.CachingProvider;
import java.net.URI;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

class MyCacheManager implements CacheManager {

    private final CachingProvider cachingProvider;
    private final URI uri;
    private final ClassLoader classLoader;
    private final Properties properties;
    private final ConcurrentMap<String, Cache<?, ?>> caches = new ConcurrentHashMap<>();
    private boolean isOpen;

    MyCacheManager(CachingProvider cachingProvider, URI uri, ClassLoader classLoader, Properties properties) {
        if (uri == null || classLoader == null)
            throw new NullPointerException();

        this.cachingProvider = cachingProvider;
        this.uri = uri;
        this.classLoader = classLoader;
        this.properties = properties;
        isOpen = true;
    }

    public CachingProvider getCachingProvider() {
        return cachingProvider;
    }

    public URI getURI() {
        return uri;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }


    public Properties getProperties() {
        return properties;
    }

    public <K, V, C extends Configuration<K, V>> Cache<K, V> createCache(String cacheName, C configuration) throws IllegalArgumentException {
        if (cacheName == null || configuration == null)
            throw new NullPointerException();

        if (isClosed())
            throw new IllegalStateException();

        if (caches.containsKey(cacheName))
            throw new CacheException();

        Cache<K, V> cache = new MyCache<>(cacheName, this, configuration);
        caches.put(cacheName, cache);

        return cache;
    }

    public <K, V> Cache<K, V> getCache(String cacheName, Class<K> keyType, Class<V> valueType) {
        throw new UnsupportedOperationException();
    }

    public <K, V> Cache<K, V> getCache(String cacheName) {
        if (cacheName == null)
            throw new NullPointerException();

        if (isClosed())
            throw new IllegalStateException();

        final Cache<?, ?> cache = caches.getOrDefault(cacheName, null);


        return (Cache<K,V>)cache;
    }

    public Iterable<String> getCacheNames() {
        if (isClosed())
                throw new IllegalStateException();

        return Collections.unmodifiableSet(new HashSet<>(caches.keySet()));
    }

    public void destroyCache(String cacheName) {
        throw new UnsupportedOperationException();
    }

    public void enableManagement(String cacheName, boolean enabled) {
        throw new UnsupportedOperationException();
    }

    public void enableStatistics(String cacheName, boolean enabled) {
        throw new UnsupportedOperationException();
    }

    public void close() {
        isOpen = false;
        caches.clear();
    }

    public boolean isClosed() {
        return !isOpen;
    }

    public <T> T unwrap(Class<T> clazz) {
        throw new UnsupportedOperationException();
    }
}