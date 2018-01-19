package ru.otus.jsr107.jmx;

import javax.cache.management.CacheStatisticsMXBean;
import java.util.concurrent.TimeUnit;

public class CacheStatisticsMXBeanImpl implements CacheStatisticsMXBean {

    private long cacheHits;
    private long cacheMisses;
    private long cacheGets;
    private long cachePuts;
    private long cacheEvictions;
    private long cacheRemovals;
    private long getTime;
    private long putTime;
    private long removeTime;

    @Override
    public void clear() {
        cacheHits = 0;
        cacheMisses = 0;
        cacheGets = 0;
        cachePuts = 0;
        cacheEvictions = 0;
        cacheRemovals = 0;
        getTime = 0;
        putTime = 0;
        removeTime = 0;
    }

    public void addHit() {
        cacheHits++;
    }

    public void addMiss() {
        cacheMisses++;
    }

    public void addCacheGet() {
        cacheGets++;
    }

    public void addCachePut() {
        cachePuts++;
    }

    public void addCacheEviction() {
        cacheEvictions++;
    }

    public void addCacheRemovals() {
        cacheRemovals++;
    }

    public void addGetTime(final long delta) {
        if (delta < 0)
            throw new IllegalArgumentException();

        getTime += delta;
    }

    public void addPutTime(final long delta) {
        if (delta < 0)
            throw new IllegalArgumentException();

        putTime += delta;
    }

    public void addRemovalTime(final long delta) {
        if (delta <= 0)
            throw new IllegalArgumentException();

        removeTime += delta;
    }


    @Override
    public long getCacheHits() {
        return cacheHits;
    }

    @Override
    public float getCacheHitPercentage() {
        return (cacheHits + cacheMisses) == 0 ? 0 : ((float)cacheHits / (cacheHits + cacheMisses)) * 100.0f;
    }

    @Override
    public long getCacheMisses() {
        return cacheMisses;
    }


    @Override
    public float getCacheMissPercentage() {
        return (cacheHits + cacheMisses) == 0 ? 0 : ((float)cacheMisses / (cacheHits + cacheMisses)) * 100.0f;
    }

    @Override
    public long getCacheGets() {
        return cacheGets;
    }

    @Override
    public long getCachePuts() {
        return cachePuts;
    }

    @Override
    public long getCacheRemovals() {
        return cacheRemovals - cacheEvictions;
    }

    @Override
    public long getCacheEvictions() {
        return cacheEvictions;
    }

    @Override
    public float getAverageGetTime() {
        long l = cacheGets == 0 ? 0 : getTime / cacheGets;
        return TimeUnit.NANOSECONDS.toMicros(l);
    }

    @Override
    public float getAveragePutTime() {
        long l = cachePuts == 0 ? 0 : putTime / cachePuts;
        return TimeUnit.NANOSECONDS.toMicros(l);
    }

    @Override
    public float getAverageRemoveTime() {
        long cacheRemovals = getCacheRemovals();
        long v = cacheRemovals == 0 ? 0 : removeTime / cacheRemovals;
        return TimeUnit.NANOSECONDS.toMicros(v);
    }
}
