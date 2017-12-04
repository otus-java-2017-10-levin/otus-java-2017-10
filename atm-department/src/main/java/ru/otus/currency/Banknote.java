package ru.otus.currency;

import ru.otus.common.CommonHelper;

import java.util.Objects;

public class Banknote {
    private final Name name;
    private final int value;
    private final Currency currency;

    Banknote(Name name, int value, Currency currency) {
        this.name = name;
        this.value = value;

        CommonHelper.throwIf(NullPointerException.class, "Currency = null", () -> currency == null);

        this.currency = currency;
    }

    public Name getName() {
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

    public enum Name {
        ONE             (1),
        TWO             (2),
        FIVE            (5),
        TEN             (10),
        TWENTY          (20),
        FIFTY           (50),
        HUNDRED         (100),
        TWO_HUNDRED     (200),
        FIVE_HUNDRED    (500),
        THOUSAND        (1000),
        TWO_THOUSAND    (2000),
        FIVE_THOUSAND   (5000);

        private final int value;

        Name(int value) {
            this.value = value;
        }

        public static Name get(long value) {
            for (Name name: Name.values()) {
                if (name.value == value)
                    return name;
            }

            return null;
        }

        @Override
        public String toString() {
            return ((Integer)value).toString();
        }
    }
}
