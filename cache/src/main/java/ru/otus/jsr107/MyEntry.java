package ru.otus.jsr107;

import javax.cache.Cache;
import javax.cache.expiry.Duration;
import java.util.concurrent.TimeUnit;

class MyEntry<K, V> implements Cache.Entry<K, V> {

    private K key;
    private V value;
    private long expiryTime;

    MyEntry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public void setExpiryTime(Duration expiryTime, long defaultValue) {
        long t;
        if (expiryTime == null) { // don't change expiry time
            t = defaultValue;
        } else {
            t = expiryTime.getAdjustedTime(TimeUnit.NANOSECONDS.toMillis(System.nanoTime()));
        }
        this.expiryTime = t;
    }

    public long getExpiryTime() {
        return expiryTime;
    }

    /**
     * Returns the key corresponding to this entry.
     *
     * @return the key corresponding to this entry
     */
    @Override
    public K getKey() {
        return key;
    }

    /**
     * Returns the value stored in the cache when this entry was created.
     *
     * @return the value corresponding to this entry
     */
    @Override
    public V getValue() {
        return value;
    }

    /**
     * Provides a standard way to access the underlying concrete cache entry
     * implementation in order to provide access to further, proprietary features.
     * <p>
     * If the provider's implementation does not support the specified class,
     * the {@link IllegalArgumentException} is thrown.
     *
     * @param clazz the proprietary class or interface of the underlying
     *              concrete cache. It is this type that is returned.
     * @return an instance of the underlying concrete cache
     * @throws IllegalArgumentException if the caching provider doesn't support
     *                                  the specified class.
     */
    @Override
    public <T> T unwrap(Class<T> clazz) {
        throw new UnsupportedOperationException();
    }
}
