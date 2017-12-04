package ru.otus.currency;

public interface Currency {
    /**
     * get all banknotes for currency. Result array sorted by Banknote.value ascending.
     *
     * @return - array of banknotes
     */
    Banknote[] getBanknotes();

    /**
     * Get banknote for specified value.
     *
     * @param value - one of the value from Name enum
     * @return - banknote object or null if there is no banknote
     * containing selected value
     */
    Banknote get(Banknote.Name value);

    /**
     * Returns currency name
     *
     * @return - AbstractCurrency name
     */
    String getCurrencyName();

    /**
     * Return sign of value or String("")
     * @return sign of ""
     */
    String getSign();

    default String formatCurrency(long value) {
        return String.format("%,d%s", value, getSign());
    }
}