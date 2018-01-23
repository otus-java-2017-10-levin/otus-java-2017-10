package ru.otus.model;

public class Statistics {
    private long misses;
    private long hits;

    public long getMisses() {
        return misses;
    }

    public void setMisses(long misses) {
        this.misses = misses;
    }

    public void setHits(long hits) {
        this.hits = hits;
    }

    public long getHits() {
        return hits;
    }
}
