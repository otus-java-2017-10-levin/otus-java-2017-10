package ru.otus;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.AccessedExpiryPolicy;
import javax.cache.expiry.Duration;
import javax.cache.expiry.EternalExpiryPolicy;
import javax.cache.spi.CachingProvider;
import java.util.Random;
import java.util.concurrent.TimeUnit;


public class App {
    public static void main(String[] args) throws InterruptedException {
        CachingProvider cachingProvider = Caching.getCachingProvider();
        CacheManager cacheManager = cachingProvider.getCacheManager();
        MutableConfiguration<String, Bigger> config = new MutableConfiguration<String, Bigger>()
                .setTypes(String.class, Bigger.class)
                .setExpiryPolicyFactory(EternalExpiryPolicy::new)
//                .setExpiryPolicyFactory(AccessedExpiryPolicy.factoryOf(new Duration(TimeUnit.SECONDS, 30)))
                .setStatisticsEnabled(true);

        Cache<String, Bigger> cache = cacheManager.createCache("simpleCache", config);

        Random rnd = new Random();
        int i1 = 300_000;
        for (int i = 0; i < i1; i++) {
            String key = (""+i).intern();
            cache.put(key, new Bigger(1_000));
        }

        while (true) {
            int i = rnd.nextInt(i1);
            String key = (""+i).intern();
            cache.get(key);

        }
    }

}