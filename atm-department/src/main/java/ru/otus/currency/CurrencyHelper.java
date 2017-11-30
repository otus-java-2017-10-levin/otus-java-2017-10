package ru.otus.currency;

/**
 *  Класс для хранения купюр для различных валют
 */
public class CurrencyHelper {
    private CurrencyHelper() {}

    public static Currency createCurrency(String name) {
        if (name == null)
            throw new IllegalArgumentException();

        name = name.toLowerCase();
        if (name.equals("ruble")) {
            return new Ruble();
        } else if (name.equals("dollar")) {
            return new Dollar();
        }
        return null;
    }
}
