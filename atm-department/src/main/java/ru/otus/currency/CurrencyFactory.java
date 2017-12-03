package ru.otus.currency;

import ru.otus.common.CommonHelper;

import java.util.HashMap;
import java.util.Map;

public class CurrencyFactory {
    private CurrencyFactory() {}

    public static Currency getCurrency(String name) {
        CommonHelper.verify(IllegalArgumentException.class, null, ()-> name == null);

        String tmp = name.toLowerCase();
        CommonHelper.verify(IllegalArgumentException.class, "Currency not found",
                () -> !currencies.containsKey(tmp));

        return currencies.get(tmp);

    }

    public static String getInfo() {
        StringBuilder res = new StringBuilder("Available currencies:\n");
        currencies.forEach((key, value) -> res.append("\t").append(key).append("\n"));
        return res.toString();
    }

    private static final Map<String, Currency> currencies = new HashMap<>();
    static {
        currencies.put("rouble", new Rouble());
        currencies.put("dollar", new Dollar());
    }
}