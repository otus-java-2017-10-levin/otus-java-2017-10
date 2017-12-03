package ru.otus.internals;

import ru.otus.currency.Banknote;
import ru.otus.currency.Currency;

public interface ATM extends Rollback {
    void addBanknote(Banknote note, int count);
    void getCash(long value) throws RuntimeException;
    long getBalance();
    Currency getCurrency();
}