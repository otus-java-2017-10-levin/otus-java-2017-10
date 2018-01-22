package ru.otus.jsr107;

import org.junit.jupiter.api.Test;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.AccessedExpiryPolicy;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import javax.cache.expiry.EternalExpiryPolicy;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class MyCacheTest {

    private MutableConfiguration<String, BigObj> config;

    private final CacheManager manager = new MyCachingProvider().getCacheManager();
    private Cache<String, BigObj> objects;

    @Test
    void testExpiryAccessed() throws InterruptedException {
        config = new MutableConfiguration<String, BigObj>()
                .setTypes(String.class, BigObj.class)
                .setExpiryPolicyFactory(AccessedExpiryPolicy.factoryOf(new Duration(TimeUnit.SECONDS, 5)))
                .setStatisticsEnabled(true);

        objects = manager.createCache("accessExpiry", config);

        for (int i=0; i< 100; i++) {
            objects.put(""+i, new BigObj());
        }

        Thread.sleep(2000);
        MyCache cache = (MyCache)objects;
        assertEquals(100, cache.size());
        System.out.println(cache);

        for (int i=0; i < 100; i+=2) {
            objects.get(""+i);
        }

        Thread.sleep(4000);

        assertEquals(50, cache.size());
        System.out.println(objects);

    }

    @Test
    void testExpiryCreated() throws InterruptedException {
        config = new MutableConfiguration<String, BigObj>()
                .setTypes(String.class, BigObj.class)
                .setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(new Duration(TimeUnit.SECONDS, 5)))
                .setStatisticsEnabled(true);
        objects = manager.createCache("createdExpire", config);


        for (int i=0; i< 100; i++) {
            objects.put(""+i, new BigObj());
        }
        MyCache cache = (MyCache)objects;
        assertEquals(100, cache.size());
        System.out.println(cache);

        Thread.sleep(1000);
        assertEquals(100, cache.size());

        Thread.sleep(1000);
        assertEquals(100, cache.size());

        Thread.sleep(1000);
        assertEquals(100, cache.size());

        Thread.sleep(1000);
        assertEquals(100, cache.size());

        Thread.sleep(2000);
        assertEquals(0, cache.size());

        System.out.println(cache);

    }

    @Test
    void testExpiryEternal() throws InterruptedException {
        config = new MutableConfiguration<String, BigObj>()
                .setTypes(String.class, BigObj.class)
                .setExpiryPolicyFactory(EternalExpiryPolicy.factoryOf())
                .setStatisticsEnabled(true);
        objects = manager.createCache("eternalExpire", config);

        for (int i=0; i< 100; i++) {
            objects.put(""+i, new BigObj());
        }

        Thread.sleep(4000);

        assertEquals(100, ((MyCache)objects).size());
        System.out.println(objects);
    }

    @Test
    void severalCacheCreate() {
        config = new MutableConfiguration<String, BigObj>()
                .setTypes(String.class, BigObj.class)
                .setExpiryPolicyFactory(EternalExpiryPolicy.factoryOf())
                .setStatisticsEnabled(true);

        objects = manager.createCache("test1", config);
        Cache<String, BigObj> objects1 = manager.createCache("test2", config);

        manager.close();

        // do not close caches
        assertEquals(false, objects.isClosed());
        assertEquals(false, objects1.isClosed());
    }
}