package ru.otus.currency;

import java.util.Objects;

public class Banknote {
    private BanknoteNames name;
    private long value;
    private String currencyName;

    Banknote(BanknoteNames name, long value, String currencyName) {
        this.name = name;
        this.value = value;
        this.currencyName = currencyName;
    }

    public BanknoteNames getName() {
        return name;
    }

    public long getValue() {
        return value;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value, currencyName);
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
                Objects.equals(currencyName, banknote.currencyName);
    }
}
