package ru.otus.currency;

import java.util.*;

/**
 * Keeps all banknotes for selected currency
 */
public abstract class Currency {

    private String currency;
    private Set<Banknote> banknotes = new HashSet<>();
    private String sign = "";

    Currency(String currency, BanknoteNames... banknotes) {
        this.currency = currency;
        this.banknotes = createBanknotesForCurrency(this.getCurrency(), banknotes);
    }

    Currency(String currency, String sign, BanknoteNames... banknotes) {
        this.sign = sign;
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
     * @return - banknote object or null if there is no banknote
     * containing selected value
     */
    public Banknote get(BanknoteNames value) {
        Banknote res = banknotes.stream().filter(e -> e.getName().equals(value)).findFirst().orElse(null);
        if (res == null)
            throw new IllegalArgumentException(value.name() + " not found in this currency");
        else
            return res;
    }

    /**
     * Returns currency name
     *
     * @return - Currency name
     */
    public String getCurrency() {
        return currency;
    }

    public String getSign() {
        return sign;
    }

    @Override
    public String toString() {
        return "Currency: "+ currency + "(" + sign+ ")";
    }

    private Set<Banknote> createBanknotesForCurrency(String currency, BanknoteNames[] banknoteNames) {
        Set<Banknote> result = new HashSet<>();

        if (currency == null || currency.equals(""))
            throw new IllegalArgumentException();

        for (BanknoteNames name: banknoteNames) {
            Integer key = banknotesValues.get(name);

            if (name == null)
                throw new IllegalArgumentException("Element = null");
            if (key == null)
                throw new IllegalStateException("Banknote name: "+ name.name() + " not found.");

            if (!result.add(new Banknote(name, key, this)))
                throw new IllegalArgumentException("Element " + name.name() + " duplicate");
        }

        return result;
    }
    private static final Map<BanknoteNames, Integer> banknotesValues = new HashMap<>();

    static  {
        banknotesValues.put(BanknoteNames.ONE, 1);
        banknotesValues.put(BanknoteNames.TWO, 2);
        banknotesValues.put(BanknoteNames.FIVE, 5);
        banknotesValues.put(BanknoteNames.TEN, 10);
        banknotesValues.put(BanknoteNames.TWENTY, 20);
        banknotesValues.put(BanknoteNames.FIFTY, 50);
        banknotesValues.put(BanknoteNames.HUNDRED, 100);
        banknotesValues.put(BanknoteNames.TWO_HUNDRED, 200);
        banknotesValues.put(BanknoteNames.FIVE_HUNDRED, 500);
        banknotesValues.put(BanknoteNames.THOUSAND, 1000);
        banknotesValues.put(BanknoteNames.TWO_THOUSAND, 2000);
        banknotesValues.put(BanknoteNames.FIVE_THOUSAND, 5000);
    }
}
