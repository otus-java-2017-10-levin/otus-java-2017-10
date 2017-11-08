package ru.otus.leaks;


import java.util.HashMap;
import java.util.Map;

public class LeakManager {

    private final static Map<LeakStrategies, Leak> leaks = new HashMap<>();
    static {
        leaks.put(LeakStrategies.HASH, new HashLeak());
        leaks.put(LeakStrategies.FINALIZE, new FinalizeLeak());
    }
    private LeakManager() {}
    private static Leak leakStrategy = null;

    public static void setStrategy(Leak strategy) {
        leakStrategy = strategy;
    }

    public static void leak(int n) {
        if (leakStrategy == null) {
            throw new IllegalStateException("Use setStrategy() first!");
        }

        leakStrategy.execute(n);
    }

    public static Leak getLeakStrategy(LeakStrategies strategy) {
        Leak result = leakStrategy;


        if (leakStrategy == null) {
            leaks.get(strategy);
        }
        return result;
    }
}
