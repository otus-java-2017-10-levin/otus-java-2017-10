package ru.otus.currency;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.otus.currency.BanknoteNames.*;

public class CurrencyTest {

    private Currency currency;

    static Banknote getBanknote(BanknoteNames name, long value) {
        return new Banknote(name, value, "Ruble");
    }

    @Test
    @DisplayName("Create currency")
    public void createCurrency() {
        currency = new Currency("Ruble",
                ONE,
                FIVE,
                TEN,
                FIFTY,
                HUNDRED,
                TWO_HUNDRED,
                FIVE_HUNDRED,
                THOUSAND,
                TWO_THOUSAND,
                FIVE_THOUSAND) {
        };

        currency = new Currency("Ruble") {
        };

    }

    @Test
    @DisplayName("Wrong create currency")
    public void createCurrencyErrors() {
        Exception e = assertThrows(IllegalArgumentException.class,
                () -> currency = new Currency("Ruble",
                        ONE,
                        FIVE,
                        FIVE,
                        TEN,
                        FIFTY,
                        HUNDRED,
                        TWO_HUNDRED,
                        FIVE_HUNDRED,
                        THOUSAND,
                        TWO_THOUSAND,
                        FIVE_THOUSAND) {
                });
        assertEquals("Element FIVE dublicate", e.getMessage());

        currency = new Currency("Ruble",
                new BanknoteNames[0]) {
        };
    }

    @Test
    @DisplayName("Wrong create currency (null in args)")
    public void createCurrencyErrorsNull() {
        Exception e = assertThrows(IllegalArgumentException.class,
                () -> currency = new Currency("Ruble",
                        ONE,
                        null,
                        FIVE,
                        TEN,
                        FIFTY,
                        HUNDRED,
                        TWO_HUNDRED,
                        FIVE_HUNDRED,
                        THOUSAND,
                        TWO_THOUSAND,
                        FIVE_THOUSAND) {
                });
        assertEquals("Element = null", e.getMessage());

        currency = new Currency("Ruble") {
        };
    }

    @Test
    @DisplayName("Wrong create currency (null  currency)")
    public void createCurrencyErrorsNullCurrency() {
        Exception e = assertThrows(IllegalArgumentException.class,
                () -> currency = new Currency("") {
                });

        assertEquals(null, e.getMessage());


        e = assertThrows(IllegalArgumentException.class,
                () -> currency = new Currency(null) {
                });

        assertEquals(null, e.getMessage());
    }

    @Test
    @DisplayName("add banknote test")
    public void addTest() {
        String cur = "Ruble";
        currency = new Currency("Ruble") {
        };

        currency.addBanknote(new Banknote(ONE, 1, cur));
        Exception e = assertThrows(IllegalArgumentException.class, () -> currency.addBanknote(new Banknote(ONE, 1, cur)));
        assertEquals(null, e.getMessage());
    }

    @Test
    @DisplayName("get banknote")
    public void getBanknote() {
        currency = new Currency("Ruble",
                ONE,
                FIVE,
                TEN,
                FIFTY,
                HUNDRED,
                TWO_HUNDRED,
                FIVE_HUNDRED,
                THOUSAND,
                TWO_THOUSAND,
                FIVE_THOUSAND) {
        };

        Banknote b = currency.get(FIVE);
        Banknote expected = new Banknote(FIVE, 5, "Ruble");
        assertEquals(true, b.equals(expected));
    }

    @Test
    @DisplayName("get bancknote")
    public void getBanknoteTest() {
        Currency currency = new Ruble();

        assertEquals(true, currency.get(ONE).equals(getBanknote(ONE, 1)));
        assertEquals(true, currency.get(FIVE).equals(getBanknote(FIVE, 5)));
        assertEquals(true, currency.get(TEN).equals(getBanknote(TEN, 10)));
        assertEquals(true, currency.get(FIFTY).equals(getBanknote(FIFTY, 50)));
        assertEquals(true, currency.get(HUNDRED).equals(getBanknote(HUNDRED, 100)));
        assertEquals(true, currency.get(TWO_HUNDRED).equals(getBanknote(TWO_HUNDRED, 200)));
        assertEquals(true, currency.get(FIVE_HUNDRED).equals(getBanknote(FIVE_HUNDRED, 500)));
        assertEquals(true, currency.get(THOUSAND).equals(getBanknote(THOUSAND, 1000)));
        assertEquals(true, currency.get(TWO_THOUSAND).equals(getBanknote(TWO_THOUSAND, 2000)));
        assertEquals(true, currency.get(FIVE_THOUSAND).equals(getBanknote(FIVE_THOUSAND, 5000)));

        assertEquals(false, currency.get(FIVE_THOUSAND).equals(getBanknote(THOUSAND, 1000)));
    }

    @Test
    @DisplayName("get all bancknotes")
    public void getAllBanknotes() {
        currency = new Currency("Ruble", ONE, FIVE, HUNDRED) {
        };

        Banknote[] arr = currency.getBanknotes();
        assertEquals(3, arr.length);
        assertEquals(true, getBanknote(ONE, 1).equals(arr[0]));
        assertEquals(true, getBanknote(FIVE, 5).equals(arr[1]));
        assertEquals(true, getBanknote(HUNDRED, 100).equals(arr[2]));

        assertEquals(null, currency.get(FIFTY));
    }
}