package ru.otus.currency;

import ru.otus.common.CommonHelper;

import java.util.*;

/**
 * Keeps all banknotes for selected currency
 */
abstract class AbstractCurrency implements Currency {

    private String currency;
    private Set<Banknote> banknotes;
    private String sign = "";

    AbstractCurrency(String currency, BanknoteName... banknotes) {
        this.currency = currency;
        this.banknotes = createBanknotesForCurrency(this.getCurrencyName(), banknotes);
    }

    AbstractCurrency(String currency, String sign, BanknoteName... banknotes) {
        this.sign = sign;
        this.currency = currency;
        this.banknotes = createBanknotesForCurrency(this.getCurrencyName(), banknotes);
    }

    @Override
    public void addBanknote(Banknote note) throws IllegalArgumentException {
        CommonHelper.verify(IllegalArgumentException.class, null,
                () ->note == null || banknotes.contains(note));

        banknotes.add(note);
    }

    @Override
    public Banknote[] getBanknotes() {
        List<Banknote> res = new ArrayList<>();
        Collections.addAll(res, banknotes.toArray(new Banknote[0]));
        res.sort(Comparator.comparingLong(Banknote::getValue));
        return res.toArray(new Banknote[0]);
    }

    @Override
    public Banknote get(BanknoteName value) {
        Banknote res = banknotes.stream().filter(e -> e.getName().equals(value)).findFirst().orElse(null);

        CommonHelper.verify(IllegalArgumentException.class, value.name() + " not found in this currency",
                () -> res == null);

        return res;
    }

    @Override
    public String getCurrencyName() {
        return currency;
    }

    @Override
    public String getSign() {
        return sign;
    }

    @Override
    public String toString() {
        return "Currency: "+ currency + "(" + sign+ ")";
    }

    private Set<Banknote> createBanknotesForCurrency(String currency, BanknoteName[] banknoteNames) {
        Set<Banknote> result = new HashSet<>();

        CommonHelper.verify(IllegalArgumentException.class, null,
                () -> currency == null || currency.equals(""));

        for (BanknoteName name: banknoteNames) {
            Integer key = banknotesValues.get(name);

            CommonHelper.verify(IllegalArgumentException.class, "Element = null",() -> name == null);
            CommonHelper.verify(IllegalStateException.class, "Banknote name: "+ name.name() + " not found.",
                    () -> key == null);
            CommonHelper.verify(IllegalArgumentException.class, "Element " + name.name() + " duplicate",
                    () -> !result.add(new Banknote(name, key, this)));
        }

        return result;
    }
    private static final Map<BanknoteName, Integer> banknotesValues = new HashMap<>();

    static  {
        banknotesValues.put(BanknoteName.ONE, 1);
        banknotesValues.put(BanknoteName.TWO, 2);
        banknotesValues.put(BanknoteName.FIVE, 5);
        banknotesValues.put(BanknoteName.TEN, 10);
        banknotesValues.put(BanknoteName.TWENTY, 20);
        banknotesValues.put(BanknoteName.FIFTY, 50);
        banknotesValues.put(BanknoteName.HUNDRED, 100);
        banknotesValues.put(BanknoteName.TWO_HUNDRED, 200);
        banknotesValues.put(BanknoteName.FIVE_HUNDRED, 500);
        banknotesValues.put(BanknoteName.THOUSAND, 1000);
        banknotesValues.put(BanknoteName.TWO_THOUSAND, 2000);
        banknotesValues.put(BanknoteName.FIVE_THOUSAND, 5000);
    }
}