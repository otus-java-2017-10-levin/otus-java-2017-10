package ru.otus.leaks;


import com.sun.istack.internal.NotNull;

public class LeakManager {

    private LeakManager() {}
    private static Leak leakStrategy = null;

    public static void setStrategy(@NotNull Leak strategy) {
        leakStrategy = strategy;
    }

    public static void leak(@NotNull int n) {
        if (leakStrategy == null) {
            throw new IllegalStateException("Use setStrategy() first!");
        }

        leakStrategy.execute(n);
    }

    public static Leak getLeakStrategy(LeakStrategies strategy) {
        Leak result = leakStrategy;

        if (leakStrategy == null) {
            switch (strategy) {
                case STRING:
                    result = new StringLeak();
                    break;
                case HASH:
                    result = new HashLeak();
                    break;
                case FINALIZE:
                    result = new FinalizeLeak();
                    break;
                default:
                    result = new StringLeak();
            }
        }
        return result;
    }
}
