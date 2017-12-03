package ru.otus.currency;

public enum BanknoteName {
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

    BanknoteName(int value) {
        this.value = value;
    }

    public static BanknoteName get(long value) {
        for (BanknoteName name: BanknoteName.values()) {
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