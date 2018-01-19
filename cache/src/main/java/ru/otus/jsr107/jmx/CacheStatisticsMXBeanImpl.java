package ru.otus.jsr107.jmx;

import javax.cache.Cache;
import javax.cache.management.CacheStatisticsMXBean;
import javax.cache.processor.EntryProcessor;
import javax.cache.processor.MutableEntry;
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

    /**
     * Clears the statistics counters to 0 for the associated Cache.
     */
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
//        if (delta <= 0)
//            throw new IllegalArgumentException();
//
//        getTime += delta;
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


    /**
     * The number of get requests that were satisfied by the cache.
     * <p>
     * {@link Cache#containsKey(Object)} is not a get request for
     * statistics purposes.
     * <p>
     * In a caches with multiple tiered storage, a hit may be implemented as a hit
     * to the cache or to the first tier.
     * <p>
     * For an {@link EntryProcessor}, a hit occurs when the
     * key exists and an entry processor can be invoked against it, even if no
     * methods of {@link Cache.Entry} or
     * {@link MutableEntry} are called.
     *
     * @return the number of hits
     */
    @Override
    public long getCacheHits() {
        return cacheHits;
    }

    /**
     * This is a measure of cache efficiency.
     * <p>
     * It is calculated as:
     * {@link #getCacheHits} divided by {@link #getCacheGets ()} * 100.
     *
     * @return the percentage of successful hits, as a decimal e.g 75.
     */
    @Override
    public float getCacheHitPercentage() {
        return (cacheHits + cacheMisses) == 0 ? 0 : ((float)cacheHits / (cacheHits + cacheMisses)) / 100.0f;
    }

    /**
     * A miss is a get request that is not satisfied.
     * <p>
     * In a simple cache a miss occurs when the cache does not satisfy the request.
     * <p>
     * {@link Cache#containsKey(Object)} is not a get request for
     * statistics purposes.
     * <p>
     * For an {@link EntryProcessor}, a miss occurs when the
     * key does not exist and therefore an entry processor cannot be invoked
     * against it.
     * <p>
     * In a caches with multiple tiered storage, a miss may be implemented as a miss
     * to the cache or to the first tier.
     * <p>
     * In a read-through cache a miss is an absence of the key in the cache that
     * will trigger a call to a CacheLoader. So it is still a miss even though the
     * cache will load and return the value.
     * <p>
     * Refer to the implementation for precise semantics.
     *
     * @return the number of misses
     */
    @Override
    public long getCacheMisses() {
        return cacheMisses;
    }

    /**
     * Returns the percentage of cache accesses that did not find a requested entry
     * in the cache.
     * <p>
     * This is calculated as {@link #getCacheMisses()} divided by
     * {@link #getCacheGets()} * 100.
     *
     * @return the percentage of accesses that failed to find anything
     */
    @Override
    public float getCacheMissPercentage() {
        return (cacheHits + cacheMisses) == 0 ? 0 : ((float)cacheMisses / (cacheHits + cacheMisses)) / 100.0f;
    }

    /**
     * The total number of requests to the cache. This will be equal to the sum of
     * the hits and misses.
     * <p>
     * A "get" is an operation that returns the current or previous value. It does
     * not include checking for the existence of a key.
     * <p>
     * In a caches with multiple tiered storage, a gets may be implemented as a get
     * to the cache or to the first tier.
     *
     * @return the number of gets
     */
    @Override
    public long getCacheGets() {
        return cacheGets;
    }

    /**
     * The total number of puts to the cache.
     * <p>
     * A put is counted even if it is immediately evicted.
     * <p>
     * Replaces, where a put occurs which overrides an existing mapping is counted
     * as a put.
     *
     * @return the number of puts
     */
    @Override
    public long getCachePuts() {
        return cachePuts;
    }

    /**
     * The total number of removals from the cache. This does not include evictions,
     * where the cache itself initiates the removal to make space.
     *
     * @return the number of removals
     */
    @Override
    public long getCacheRemovals() {
        return cacheRemovals - cacheEvictions;
    }

    /**
     * The total number of evictions from the cache. An eviction is a removal
     * initiated by the cache itself to free up space. An eviction is not treated as
     * a removal and does not appear in the removal counts.
     *
     * @return the number of evictions
     */
    @Override
    public long getCacheEvictions() {
        return cacheEvictions;
    }

    /**
     * The mean time to execute gets.
     * <p>
     * In a read-through cache the time taken to load an entry on miss is not
     * included in get time.
     *
     * @return the time in µs
     */
    @Override
    public float getAverageGetTime() {
        long l = cacheGets == 0 ? 0 : getTime / cacheGets;
        return TimeUnit.NANOSECONDS.toMicros(l);
    }

    /**
     * The mean time to execute puts.
     *
     * @return the time in µs
     */
    @Override
    public float getAveragePutTime() {
        long l = cachePuts == 0 ? 0 : putTime / cachePuts;
        return TimeUnit.NANOSECONDS.toMicros(l);
    }

    /**
     * The mean time to execute removes.
     *
     * @return the time in µs
     */
    @Override
    public float getAverageRemoveTime() {
        long cacheRemovals = getCacheRemovals();
        long v = cacheRemovals == 0 ? 0 : removeTime / cacheRemovals;
        return TimeUnit.NANOSECONDS.toMicros(v);
    }
}
