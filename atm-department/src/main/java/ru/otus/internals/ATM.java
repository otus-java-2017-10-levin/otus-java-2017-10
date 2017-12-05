package ru.otus.internals;

import ru.otus.currency.Banknote;
import ru.otus.currency.Currency;

import java.util.Map;

public interface ATM extends Rollback<ATM>, IdentifiedObject<Integer> {
    void addBanknote(Banknote note, int count);
    void getCash(long value) throws RuntimeException;
    long getBalance();
    Map<Banknote, Integer> getBanknotes();

}