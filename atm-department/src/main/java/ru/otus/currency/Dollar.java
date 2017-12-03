package ru.otus.currency;


final class Dollar extends AbstractCurrency {
    Dollar() {
        super("Dollar",  "$",
                BanknoteName.ONE,
                BanknoteName.TWO,
                BanknoteName.FIVE,
                BanknoteName.TEN,
                BanknoteName.FIFTY,
                BanknoteName.HUNDRED);
    }
}