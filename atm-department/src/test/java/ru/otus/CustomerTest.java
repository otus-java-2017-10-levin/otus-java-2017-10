package ru.otus;

import org.junit.jupiter.api.Test;
import ru.otus.currency.CurrencyFactory;
import ru.otus.internals.ATM;
import ru.otus.internals.ATMBuilder;

class CustomerTest {

    @Test
    void testWork() {
        ATM atm = new ATMBuilder().build();

        ATMCustomer customer = new ATMCustomer(CurrencyFactory.getCurrency(CurrencyFactory.Currencies.ROUBLE));

        for (int i = 0; i < 1000; i++)
            customer.work(atm);

    }
}
