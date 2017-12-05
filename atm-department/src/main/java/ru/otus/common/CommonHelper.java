package ru.otus.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;
public class CommonHelper {

    final static Logger logger = LogManager.getLogger(CommonHelper.class);
    /**
     * Test condition {@code condition} and if false throws an exception of type {@code e} with message {@code errorMessage}
     *
     * @param e            - exception type
     * @param errorMessage - error message
     * @param condition    - condition to condition
     */
    public static <T extends Exception> void throwIf(Class<T> e, String errorMessage, Supplier<Boolean> condition) throws T {
        if (condition.get()) {
            T exception;
            try {
                if (errorMessage == null || errorMessage.equals("")) {
                    exception = e.getConstructor().newInstance();
                } else
                    exception = e.getConstructor(String.class).newInstance(errorMessage);

                throw exception;
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e1) {
                logger.error("", e1);
            }
        }
    }
}
