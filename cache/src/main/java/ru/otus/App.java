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
        char c = 'a'+'b';
        CacheManager cacheManager = cachingProvider.getCacheManager();
//configure the cache
        MutableConfiguration<String, Bigger> config =
                new MutableConfiguration<String, Bigger>()
                        .setTypes(String.class, Bigger.class)
                        .setExpiryPolicyFactory(AccessedExpiryPolicy.factoryOf(Duration.ONE_MINUTE))
                        .setStatisticsEnabled(true);
//create the cache
        Cache<String, Bigger> cache = cacheManager.createCache("simpleCache", config);
//cache operations
        long counter = 0;
        while (true) {
            cache.put(""+counter, new Bigger());
        }
    }
}