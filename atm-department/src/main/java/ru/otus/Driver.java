package ru.otus;

import ru.otus.currency.BanknoteNames;
import ru.otus.currency.Currency;
import ru.otus.currency.CurrencyHelper;
import ru.otus.internals.ATM;
import ru.otus.internals.ATMBuilder;

import java.util.function.Function;

public class Driver {

    private static ATM atm;
    private static final String RUBLE = "Ruble";
    public static void main(String[] args) {
        Driver driver = new Driver();

        driver.run(args);
    }

    public void run(String[] args) {
        Currency currency = CurrencyHelper.createCurrency(RUBLE);
        atm = new ATMBuilder().addBanknote(currency.get(BanknoteNames.FIVE_THOUSAND), 100L)
                .addBanknote(currency.get(BanknoteNames.TWO_THOUSAND), 100L)
                .addBanknote(currency.get(BanknoteNames.THOUSAND), 100L)
                .addBanknote(currency.get(BanknoteNames.FIVE_HUNDRED), 100L)
                .build();

        printATM(atm);
        getCash(256000, currency);
        printATM(atm);

    }

    private void getCash(long value, Currency currency) {
        try {
            atm.getCash(value, currency);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    private void printATM(ATM atm) {
        System.out.println(atm);
    }
}
