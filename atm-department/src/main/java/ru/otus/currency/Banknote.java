package ru.otus.currency;

import java.util.Objects;

public class Banknote {
    private final BanknoteNames name;
    private final int value;
    private final Currency currency;

    Banknote(BanknoteNames name, int value, Currency currency) {
        this.name = name;
        this.value = value;

        if (currency == null)
            throw new NullPointerException("Currency = null");

        this.currency = currency;
    }

    public BanknoteNames getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public String getCurrencyName() {
        return currency.getCurrency();
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
