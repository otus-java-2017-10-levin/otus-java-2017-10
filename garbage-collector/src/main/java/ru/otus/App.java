package ru.otus;

import ru.otus.leaks.LeakManager;
import ru.otus.leaks.LeakStrategies;

import java.lang.management.ManagementFactory;

public class App {
    public static void main(String[] args) {

        System.out.print(ManagementFactory.getRuntimeMXBean().getName());
        LeakManager.setStrategy(LeakManager.getLeakStrategy(LeakStrategies.FINALIZE));
        while (true) {
            LeakManager.leak(100);
        }
    }
}
