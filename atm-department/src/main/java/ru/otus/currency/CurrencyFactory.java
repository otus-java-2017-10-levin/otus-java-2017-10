package ru.otus.currency;

import ru.otus.common.Utils;

import java.util.HashMap;
import java.util.Map;

public class CurrencyFactory {

    public enum Currencies {
        ROUBLE
    }

    private CurrencyFactory() {}

    public static Currency getCurrency(Currencies name) {
        Utils.throwIf(IllegalArgumentException.class, null, ()-> name == null);

        Utils.throwIf(IllegalArgumentException.class, "Currency not found",
                () -> !currencies.containsKey(name));

        return currencies.get(name);
    }

    private static final Map<Currencies, Currency> currencies = new HashMap<>();
    static {
        currencies.put(Currencies.ROUBLE, new Rouble());
    }
}