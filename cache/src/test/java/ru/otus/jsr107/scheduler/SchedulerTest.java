package ru.otus.jsr107.scheduler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

class SchedulerTest {
    int i = 0;
    private Logger LOGGER = LogManager.getLogger(SchedulerTest.class.getSimpleName());
    @Test
    void test() {
        Scheduler scheduler = new Scheduler();

        scheduler.addTask("1", () -> {
            incr();
            LOGGER.error(i);
        }, 100, TimeUnit.MILLISECONDS);

    }

    private void incr() {
        i +=2;
    }
}