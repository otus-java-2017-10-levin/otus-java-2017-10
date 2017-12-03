package ru.otus.currency;

import ru.otus.common.CommonHelper;

import java.util.Objects;

public class Banknote {
    private final BanknoteName name;
    private final int value;
    private final Currency currency;

    Banknote(BanknoteName name, int value, Currency currency) {
        this.name = name;
        this.value = value;

        CommonHelper.throwIf(NullPointerException.class, "Currency = null", () -> currency == null);

        this.currency = currency;
    }

    public BanknoteName getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public String getCurrencyName() {
        return currency.getCurrencyName();
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value, getCurrencyName());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (!(obj instanceof Banknote))
            return false;

        Banknote banknote = (Banknote) obj;
        return value == banknote.value &&
                Objects.equals(name, banknote.name) &&
                Objects.equals(getCurrencyName(), banknote.getCurrencyName());
    }

    public String getSign() {
        return currency.getSign();
    }

    @Override
    public String toString() {
        return "Banknote:" + name + ";" +
                value + getSign() + ";" +
                getCurrencyName();
    }
}
