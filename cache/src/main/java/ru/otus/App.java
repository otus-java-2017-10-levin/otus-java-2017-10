package ru.otus;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.AccessedExpiryPolicy;
import javax.cache.expiry.Duration;
import javax.cache.spi.CachingProvider;
import java.util.Random;
import java.util.concurrent.TimeUnit;


public class App {
    public static void main(String[] args) throws InterruptedException {
        CachingProvider cachingProvider = Caching.getCachingProvider();
        CacheManager cacheManager = cachingProvider.getCacheManager();
        MutableConfiguration<String, Bigger> config = new MutableConfiguration<String, Bigger>()
                .setTypes(String.class, Bigger.class)
                .setExpiryPolicyFactory(AccessedExpiryPolicy.factoryOf(new Duration(TimeUnit.SECONDS, 30)))
                .setStatisticsEnabled(true);

        Cache<String, Bigger> cache = cacheManager.createCache("simpleCache", config);


        Random rnd = new Random();
        while (true) {
            int i = rnd.nextInt(10_000_000);

            if (i < 2_000_000)
                cache.put("" + i, new Bigger(10));
            else {
                int ind = i % 2_000_000;
                cache.get("" + ind);
            }
        }
    }

}