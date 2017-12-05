package ru.otus.internals;

import ru.otus.currency.Banknote;

import java.util.Map;

/**
 * Chains of responsibility pattern
 */
interface Handler {
    /**
     * Returns number of banknotes of Name or max param for given sum.
     * If sum / banknote prize > max, then returns max. Else return the division result.
     * @param sum - total cash to get
     * @param atmCash - total number of each banknote in the ATM
     * @param result - resulting map containing sum in number of banknotes.
     * @return - total cash handled in this handler. If sum != return value,
     * we cannot give money (insufficient banknotes, or wrong sum)
     */
    long handle(long sum, Map<Banknote, Integer> atmCash, Map<Banknote, Integer> result);

    /**
     * Setting next handler
     *
     * @param handler - next handler
     */
    void setNext(Handler handler);
}
