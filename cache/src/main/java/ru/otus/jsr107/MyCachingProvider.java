package ru.otus.jsr107;

import javax.cache.CacheManager;
import javax.cache.configuration.OptionalFeature;
import javax.cache.spi.CachingProvider;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Properties;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class MyCachingProvider implements CachingProvider {

    private static final String DEFAULT_URI_STRING = "my-cache-jsr107";
    private static final URI DEFAULT_URI;

    static {
        try {
            DEFAULT_URI = new URI(DEFAULT_URI_STRING);
        } catch (URISyntaxException e) {
            throw new javax.cache.CacheException(e);
        }
    }

    private final Map<ClassLoader, ConcurrentMap<URI, CacheManager>> cacheManagers = new WeakHashMap<>();

    public CacheManager getCacheManager(URI uri, ClassLoader classLoader, Properties properties) {
        uri = uri == null ? getDefaultURI() : uri;
        classLoader = classLoader == null ? getDefaultClassLoader() : classLoader;

        CacheManager cacheManager;
        ConcurrentMap<URI, CacheManager> map = cacheManagers.get(classLoader);
        if (map != null) {
            cacheManager = map.getOrDefault(uri, null);

            if (cacheManager == null) {
                cacheManager = new MyCacheManager(this, uri, classLoader, properties);
                map.put(uri, cacheManager);
            }
        } else {
            map = new ConcurrentHashMap<>();
            cacheManager = new MyCacheManager(this, uri, classLoader, properties);
            map.put(uri, cacheManager);
        }

        cacheManagers.putIfAbsent(classLoader, map);

        return cacheManager;
    }

      public ClassLoader getDefaultClassLoader() {
        return this.getClass().getClassLoader();
    }

    public URI getDefaultURI() {
        return DEFAULT_URI;
    }

    public Properties getDefaultProperties() {
        return null;
    }


    public CacheManager getCacheManager(URI uri, ClassLoader classLoader) {
        return getCacheManager(uri, classLoader, null);
    }


    public CacheManager getCacheManager() {
        return getCacheManager(null, null, null);
    }

    public void close() {
        throw new UnsupportedOperationException();
    }

    public void close(ClassLoader classLoader) {
        throw new UnsupportedOperationException();
    }

    public void close(URI uri, ClassLoader classLoader) {
        throw new UnsupportedOperationException();
    }

    public boolean isSupported(OptionalFeature optionalFeature) {
        return false;
    }
}