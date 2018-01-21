package ru.otus.jsr107.scheduler;

import java.util.Map;
import java.util.concurrent.*;

public class Scheduler {
    private final Map<Integer, ScheduledFuture<?>> map = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler;


    public Scheduler(int size) {
        if (size < 1 || size > 100)
            throw new IllegalArgumentException();
        scheduler = Executors.newScheduledThreadPool(size);
    }

    public void addTask(String key, Runnable runnable, long interval, TimeUnit timeUnit) {

        if (runnable == null || key == null)
            throw new IllegalArgumentException();

        if (timeUnit == TimeUnit.MICROSECONDS || timeUnit == TimeUnit.NANOSECONDS)
            throw new IllegalArgumentException("Time interval must be greater than MICROSECONDS");

        removeTask(key);
        try {
            ScheduledFuture future = scheduler.scheduleWithFixedDelay(runnable, interval, interval, timeUnit);
            map.put(key.hashCode(), future);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeTask(String key) {
        if (key == null)
            throw new IllegalArgumentException();
        int realKey = key.hashCode();
        if (map.containsKey(realKey)) {
            ScheduledFuture<?> scheduledFuture = map.get(realKey);
            scheduledFuture.cancel(false);
            while (!scheduledFuture.isDone()) {
            }
            map.remove(realKey);
        }
    }

    public void close() {
        scheduler.shutdown();
    }


    @Override
    public String toString() {
        return "Scheduler{" +
                ",\n map " + map.size() +
                ",\n scheduler=" + scheduler +
                '}';
    }
}
