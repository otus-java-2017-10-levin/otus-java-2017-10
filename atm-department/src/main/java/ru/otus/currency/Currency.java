package ru.otus.currency;

import java.util.*;

/**
 * Keeps all banknotes for selected currency
 */
public abstract class Currency {

    Currency(String currency, BanknoteNames... banknotes) {
        this.currency = currency;
        this.banknotes = createBanknotesForCurrency(this.getCurrency(), banknotes);
    }

    /**
     * Adds Banknote to Currency
     *
     * @param note - Banknote object
     * @throws IllegalArgumentException - throw exception if note == null ||
     *                                  note is already in Currency
     */
    void addBanknote(Banknote note) throws IllegalArgumentException {
        if (note == null || banknotes.contains(note))
            throw new IllegalArgumentException();

        banknotes.add(note);
    }

    /**
     * get all banknotes for currency. Result array sorted by Banknote.value ascending.
     *
     * @return - array of banknotes
     */
    public Banknote[] getBanknotes() {
        List<Banknote> res = new ArrayList<>();
        Collections.addAll(res, banknotes.toArray(new Banknote[0]));
        res.sort(Comparator.comparingLong(Banknote::getValue));
        return res.toArray(new Banknote[0]);
    }

    /**
     * Get banknote for specified value.
     *
     * @param value - one of the value from BanknoteName enum
     * @return - banknot object or null if there is no banknote
     * containing selected value
     */
    public Banknote get(BanknoteNames value) {
        return banknotes.stream().filter(e -> e.getName().equals(value)).findFirst().orElse(null);
    }

    /**
     * Returns currency name
     *
     * @return - Currency name
     */
    public String getCurrency() {
        return currency;
    }

    private Set<Banknote> createBanknotesForCurrency(String currency, BanknoteNames[] banknoteNames) {
        Set<Banknote> result = new HashSet<>();

        if (currency == null || currency.equals(""))
            throw new IllegalArgumentException();

        for (BanknoteNames name: banknoteNames) {
            Long key = banknotesValues.get(name);

            if (name == null)
                throw new IllegalArgumentException("Element = null");
            if (key == null)
                throw new IllegalStateException("Banknote name: "+ name + " not found.");

            if (!result.add(new Banknote(name, key, currency)))
                throw new IllegalArgumentException("Element " + name + " dublicate");
        }

        return result;
    }

    private String currency;
    private Set<Banknote> banknotes = new HashSet<>();

    private static Map<BanknoteNames, Long> banknotesValues = new HashMap<>();

    static  {
        banknotesValues.put(BanknoteNames.ONE, 1L);
        banknotesValues.put(BanknoteNames.TWO, 2L);
        banknotesValues.put(BanknoteNames.FIVE, 5L);
        banknotesValues.put(BanknoteNames.TEN, 10L);
        banknotesValues.put(BanknoteNames.TWENTY, 20L);
        banknotesValues.put(BanknoteNames.FIFTY, 50L);
        banknotesValues.put(BanknoteNames.HUNDRED, 100L);
        banknotesValues.put(BanknoteNames.TWO_HUNDRED, 200L);
        banknotesValues.put(BanknoteNames.FIVE_HUNDRED, 500L);
        banknotesValues.put(BanknoteNames.THOUSAND, 1000L);
        banknotesValues.put(BanknoteNames.TWO_THOUSAND, 2000L);
        banknotesValues.put(BanknoteNames.FIVE_THOUSAND, 5000L);
    }
}
