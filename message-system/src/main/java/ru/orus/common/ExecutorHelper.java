package ru.orus.common;
/*
 *  Copyright by Flow on 27.02.2018.
 
    Description here
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class ExecutorHelper {
    private final static Logger LOGGER = LoggerFactory.getLogger(ExecutorHelper.class);

    public static void shutdownAndAwaitTermination(ExecutorService pool, long timeout) {
        LOGGER.debug("Terminating pool..");
        pool.shutdown(); // Disable new tasks from being submitted
        try {
            // Wait a while for existing tasks to terminate
            LOGGER.debug("Awaiting termination for " + timeout + " sec.");
            if (!pool.awaitTermination(timeout, TimeUnit.SECONDS)) {
                LOGGER.debug("Shutting down now...");
                pool.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!pool.awaitTermination(timeout, TimeUnit.SECONDS))
                    LOGGER.error("Pool did not terminate");
            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            LOGGER.debug("Interrupted exception caught. Interrupt thread.");
            pool.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
    }
}
