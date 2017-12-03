package ru.otus.internals;

import ru.otus.currency.Banknote;
import ru.otus.currency.Currency;

public interface ATM {
    void addBanknote(Banknote note, int count);
    void getCash(long value) throws RuntimeException;
    long getBalance();
    void changeCurrency(Currency currency);
    Currency getCurrency();
}