package ru.otus;


import ru.otus.currency.Currency;
import ru.otus.internals.ATM;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ExecutionService for ATMCustomers
 */

public class CustomerPool {

    private ExecutorService pool = Executors.newCachedThreadPool();
    private Currency currency;

    public CustomerPool(Currency currency) {
        this.currency = currency;
    }

    /**
     * Apply customers to specified {@code atm}
     * Add new task to CustomerPool.
     *
     * @param atm - atm to add
     */
    public void addATM(ATM atm) {
        int randomBoubd = 2_000;
        Random random = new Random();
        int i = random.nextInt(randomBoubd);

        pool.submit(() -> {
            while (true) {
                new ATMCustomer(currency).work(atm);
                Thread.sleep(i+randomBoubd);
            }
        });
    }
}
