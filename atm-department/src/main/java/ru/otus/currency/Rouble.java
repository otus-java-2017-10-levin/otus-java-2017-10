package ru.otus.currency;

public final class Rouble extends AbstractCurrency {
    public Rouble() {
        super("Rouble",  "\u20bd",   BanknoteName.ONE,
                BanknoteName.TWO,
                BanknoteName.FIVE,
                BanknoteName.TEN,
                BanknoteName.FIFTY,
                BanknoteName.HUNDRED,
                BanknoteName.TWO_HUNDRED,
                BanknoteName.FIVE_HUNDRED,
                BanknoteName.THOUSAND,
                BanknoteName.TWO_THOUSAND,
                BanknoteName.FIVE_THOUSAND);
    }
}