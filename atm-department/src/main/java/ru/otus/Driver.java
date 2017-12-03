package ru.otus;

import ru.otus.currency.BanknoteName;
import ru.otus.currency.Currency;
import ru.otus.currency.CurrencyFactory;
import ru.otus.internals.ATM;
import ru.otus.internals.ATMBuilder;


@SuppressWarnings("IfCanBeSwitch")
class Driver {

    private static final String RUBLE = "Rouble";

    public static void main(String[] args) {
        Driver driver = new Driver();

        driver.run();
    }

    private void run() {
        Currency currency = CurrencyFactory.getCurrency(RUBLE);
        try {
            ATM atm = new ATMBuilder().addBanknote(currency.get(BanknoteName.FIVE_THOUSAND), 100)
                    .addBanknote(currency.get(BanknoteName.TWO_THOUSAND), 100)
                    .addBanknote(currency.get(BanknoteName.THOUSAND), 100)
                    .addBanknote(currency.get(BanknoteName.FIVE_HUNDRED), 100)
                    .build();

            System.out.println(atm);
            atm.getCash(100_000);
            System.out.println(atm);
            atm.addBanknote(currency.get(BanknoteName.ONE), 1000);
            System.out.println(atm);

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}