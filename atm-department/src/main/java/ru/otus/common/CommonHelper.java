package ru.otus.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.otus.ATMCustomer;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Random;
import java.util.function.Predicate;
import java.util.function.Supplier;
public class CommonHelper {

    final static Logger logger = LogManager.getLogger(ATMCustomer.class);
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
//                e1.printStackTrace();
                logger.error("", e1);
            }
        }
    }

    //TODO
    public static <T> T getRandom(T[] array, Predicate<T> test) {
        throwIf(IllegalArgumentException.class, null,
                () -> array == null || array.length == 0);

        Random rnd = new Random();
        int count = array.length;
        int random = rnd.nextInt(count)+1;

        T res = Arrays.stream(array).filter(test::test).findFirst().orElse(null);

        CommonHelper.throwIf(IllegalStateException.class, "no such element " + random, () -> res == null);
        return res;
    }
}
