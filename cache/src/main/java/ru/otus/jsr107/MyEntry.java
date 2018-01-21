package ru.otus.jsr107;

import javax.cache.Cache;
import javax.cache.expiry.Duration;
import java.util.concurrent.TimeUnit;

class MyEntry<K, V> implements Cache.Entry<K, V> {

    private final K key;
    private final V value;
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


    @Override
    public K getKey() {
        return key;
    }

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public <T> T unwrap(Class<T> clazz) {
        throw new UnsupportedOperationException();
    }
}
