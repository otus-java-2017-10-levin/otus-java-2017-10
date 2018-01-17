package ru.otus;


import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.AccessedExpiryPolicy;
import javax.cache.expiry.Duration;
import javax.cache.spi.CachingProvider;


/*
    Задача:
    Напишите свой cache engine с soft references.

    Формулировка задачи. Необходимо поддержать:
    1. JSR-107
    1. L1 cache
    2. Eviction policy
    3. Cache statistics
    4. JMX
 */
public class App 
{
    public static void main( String[] args )
    {
        //resolve a cache manager
        CachingProvider cachingProvider = Caching.getCachingProvider();
        CacheManager cacheManager = cachingProvider.getCacheManager();
//configure the cache
        MutableConfiguration<String, Integer> config =
                new MutableConfiguration<String, Integer>()
                        .setTypes(String.class, Integer.class)
                        .setExpiryPolicyFactory(AccessedExpiryPolicy.factoryOf(Duration.ONE_HOUR))
                        .setStatisticsEnabled(true);
//create the cache
        Cache<String, Integer> cache = cacheManager.createCache("simpleCache", config);
//cache operations
        String key = "key";
        Integer value1 = 1;
        cache.put("key", value1);
        Integer value2 = cache.get(key);
        assert value1.equals(1);
        cache.remove(key);
        assert cache.get(key) == null;
    }
}
