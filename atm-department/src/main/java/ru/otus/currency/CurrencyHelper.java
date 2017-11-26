package ru.otus.currency;

/**
 *  Класс для хранения купюр для различных валют
 */
public class CurrencyHelper {
    private CurrencyHelper() {}

    public static Currency createCurrency(String name) {
        if (name == null)
            throw new IllegalArgumentException();

        if (name.toLowerCase().equals("ruble")) {
            return new Ruble();
        }
        return null;
    }
}
