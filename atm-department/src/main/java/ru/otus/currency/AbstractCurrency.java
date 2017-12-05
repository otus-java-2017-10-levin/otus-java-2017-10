package ru.otus.currency;

import ru.otus.common.CommonHelper;

import java.util.*;

/**
 * Abstract currency for creating concrete currency.
 * Has banknote names enumeration from with initial values e.g.
 *
 *          FIVE_THOUSAND = 5000 etc.
 *
 * This class has only final fields and getters for immutability.
 * So you should not extends it for adding setters methods.
 *
 * Example:
 *
 *   final class Rouble extends AbstractCurrency {
 *       Rouble() {
 *           super("Rouble",  "\u20bd",   Banknote.Name.ONE,
 *                   Banknote.Name.TWO,
 *                   Banknote.Name.FIVE,
 *                   Banknote.Name.TEN,
 *                   Banknote.Name.FIFTY,
 *                   Banknote.Name.HUNDRED,
 *                   Banknote.Name.TWO_HUNDRED,
 *                   Banknote.Name.FIVE_HUNDRED,
 *                   Banknote.Name.THOUSAND,
 *                   Banknote.Name.TWO_THOUSAND,
 *                   Banknote.Name.FIVE_THOUSAND);
 *       }
 *   }
 *
 *   Immutable
 */


abstract class AbstractCurrency implements Currency {

    private final String currency;
    private final Set<? extends Banknote> banknotes;
    private final String sign;

    AbstractCurrency(String currency, Banknote.Name... banknotes) {
        this.sign = "";
        this.currency = currency;
        this.banknotes = createBanknotesForCurrency(this.getCurrencyName(), banknotes);
    }

    AbstractCurrency(String currency, String sign, Banknote.Name... banknotes) {
        this.sign = sign;
        this.currency = currency;
        this.banknotes = createBanknotesForCurrency(this.getCurrencyName(), banknotes);
    }

    @Override
    public Banknote[] getBanknotes() {
        List<Banknote> res = new ArrayList<>();
        Collections.addAll(res, banknotes.toArray(new Banknote[0]));
        res.sort(Comparator.comparingLong(Banknote::getValue));
        return res.toArray(new Banknote[0]);
    }

    @Override
    public Banknote get(Banknote.Name value) {
        Banknote res = banknotes.stream().filter(e -> e.getName().equals(value)).findFirst().orElse(null);

        CommonHelper.throwIf(IllegalArgumentException.class, value.name() + " not found in this currency",
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

    private Set<? extends Banknote> createBanknotesForCurrency(String currency, Banknote.Name[] names) {
        Set<Banknote> result = new HashSet<>();

        CommonHelper.throwIf(IllegalArgumentException.class, null,
                () -> currency == null || currency.equals(""));

        for (Banknote.Name name: names) {
            Integer key = banknotesValues.get(name);

            CommonHelper.throwIf(IllegalArgumentException.class, "Element = null",() -> name == null);
            CommonHelper.throwIf(IllegalStateException.class, "Banknote name: "+ name.name() + " not found.",
                    () -> key == null);
            CommonHelper.throwIf(IllegalArgumentException.class, "Element " + name.name() + " duplicate",
                    () -> !result.add(Banknote.of(name, key, this)));
        }

        return result;
    }

    private static final Map<Banknote.Name, Integer> banknotesValues = new HashMap<>();
    static  {
        banknotesValues.put(Banknote.Name.ONE, 1);
        banknotesValues.put(Banknote.Name.TWO, 2);
        banknotesValues.put(Banknote.Name.FIVE, 5);
        banknotesValues.put(Banknote.Name.TEN, 10);
        banknotesValues.put(Banknote.Name.TWENTY, 20);
        banknotesValues.put(Banknote.Name.FIFTY, 50);
        banknotesValues.put(Banknote.Name.HUNDRED, 100);
        banknotesValues.put(Banknote.Name.TWO_HUNDRED, 200);
        banknotesValues.put(Banknote.Name.FIVE_HUNDRED, 500);
        banknotesValues.put(Banknote.Name.THOUSAND, 1000);
        banknotesValues.put(Banknote.Name.TWO_THOUSAND, 2000);
        banknotesValues.put(Banknote.Name.FIVE_THOUSAND, 5000);
    }
}