package ru.otus.jsr107;

import javax.cache.Cache;
import java.util.concurrent.ScheduledFuture;

class MyEntry<K, V> implements Cache.Entry<K, V> {

    private K key;
    private V value;
    private long expiryTime;
    private ScheduledFuture<?> task;

    MyEntry(K key, V value, ScheduledFuture<?> task) {
        this.key = key;
        this.value = value;
        this.task = task;
    }

    public void setExpiryTime(long expiryTime) {
        this.expiryTime = expiryTime;
    }

    public long getExpiryTime() {
        return expiryTime;
    }

    public void dispose() {
        task.cancel(false);
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
