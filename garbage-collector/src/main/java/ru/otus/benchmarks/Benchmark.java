package ru.otus.benchmarks;


import ru.otus.leaks.LeakManager;
import ru.otus.leaks.LeakStrategies;

public class Benchmark implements BenchmarkMBean {
    private volatile LeakStrategies leakStrategy;
    private volatile GCCount young;
    private volatile GCCount old;

    @SuppressWarnings("InfiniteLoopStatement")
    public void run() {
        LeakManager.setStrategy(LeakManager.getLeakStrategy(leakStrategy));

        long interval = 60 * 1000;
        long time = 0;
        while (true) {
            long start = System.currentTimeMillis();
            try {
                LeakManager.leak(1000);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
            long end = System.currentTimeMillis();
            time += end - start;
            if (time > interval) {
                System.out.println("Last "+ (double)time / 1000.0 + "s. Stats:");
                time = 0;
                System.out.println("Total young collectors ("+young.getCollector()+"):");
                young.getInfo();
                System.out.println("Total old collectors ("+old.getCollector()+"):");
                old.getInfo();
            }
        }
    }

    @Override
    public void setLeakStrategy(LeakStrategies strategy) {
        leakStrategy = strategy;
    }

    @Override
    public void setYoungCollector(String collector) {
        young = new GCCount(collector);
    }

    @Override
    public void setOldCollector(String collector) {
        old = new GCCount(collector);
    }
}
