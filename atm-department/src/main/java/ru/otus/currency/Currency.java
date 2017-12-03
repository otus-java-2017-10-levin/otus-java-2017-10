package ru.otus.currency;

public interface Currency {
    /**
     * Adds Banknote to AbstractCurrency
     *
     * @param note - Banknote object
     * @throws IllegalArgumentException - throw exception if note == null ||
     *                                  note is already in AbstractCurrency
     */
    void addBanknote(Banknote note) throws IllegalArgumentException;

    /**
     * get all banknotes for currency. Result array sorted by Banknote.value ascending.
     *
     * @return - array of banknotes
     */
    public Banknote[] getBanknotes();

    /**
     * Get banknote for specified value.
     *
     * @param value - one of the value from BanknoteName enum
     * @return - banknote object or null if there is no banknote
     * containing selected value
     */
    public Banknote get(BanknoteName value);

    /**
     * Returns currency name
     *
     * @return - AbstractCurrency name
     */
    public String getCurrencyName();

    /**
     * Return sign of value or String("")
     * @return sign of ""
     */
    public String getSign();
}
