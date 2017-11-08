package ru.otus.leaks;


public class LeakManager {

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
            switch (strategy) {
                case HASH:
                    result = new HashLeak();
                    break;
                case FINALIZE:
                    result = new FinalizeLeak();
                    break;
                default:
                    result = new HashLeak();
            }
        }
        return result;
    }
}
