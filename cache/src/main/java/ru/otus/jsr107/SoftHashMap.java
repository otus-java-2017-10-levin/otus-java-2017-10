package ru.otus.jsr107;


import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

class SoftHashMap<K, V> implements Map<K, V> {

    private final Map<K, SoftReference<V>> map = new ConcurrentHashMap<>();
    private final ReferenceQueue<V> queue = new ReferenceQueue<>();

    @Override
    public int size() {
        clean();
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        clean();
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public V get(Object key) {
        if (key == null)
            throw new IllegalArgumentException();

        clean();

        synchronized (map) {
            if (map.containsKey(key)) {

                SoftReference<V> ref = map.get(key);
                if (isGarbageCollected(ref)) {
                    throw new IllegalStateException();
                } else {
                    return ref.get();
                }
            }
        }
        return null;
    }

    private void clean() {
        Reference<? extends V> poll;
        while ((poll = queue.poll()) != null) {
            map.values().remove(poll);
        }
    }

    private boolean isGarbageCollected(SoftReference ref) {
        return ref != null && ref.get() == null;
    }

    @Override
    public V put(K key, V value) {
        if (key == null || value == null)
            throw new NullPointerException();

        clean();

        if (!map.containsKey(key)) {
            map.put(key, new SoftReference<>(value, queue));
            return null;
        }

        SoftReference<V> ref = map.get(key);
        if (isGarbageCollected(ref)) {
            remove(key);
            map.put(key, new SoftReference<>(value, queue));
            return null;
        }

        return ref.get();
    }

    @Override
    public V remove(Object key) {
        clean();
        SoftReference<V> remove = map.remove(key);
        return remove == null ? null : remove.get();
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Set<K> keySet() {
        clean();
        return map.keySet();
    }

    @Override
    public Collection<V> values() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return map.entrySet().stream().map(entry -> new Entry<>(entry.getKey(), entry.getValue() == null ? null : entry.getValue().get())).collect(Collectors.toSet());
    }

    private final class Entry<K,V> implements Map.Entry<K, V> {

        private final K key;
        private final V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
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
        public V setValue(V value) {
            throw new UnsupportedOperationException();
        }
    }
}
