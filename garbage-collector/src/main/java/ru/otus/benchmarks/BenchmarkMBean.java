package ru.otus.benchmarks;

import ru.otus.leaks.LeakStrategies;

public interface BenchmarkMBean {
    void setLeakStrategy(LeakStrategies strategy);
    void setYoungCollector(String collector);
    void setOldCollector(String collector);
}
