package ru.otus.currency;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.otus.currency.Banknote.Name.*;

class CurrencyTest {

    private static Currency currency = CurrencyFactory.getCurrency(CurrencyFactory.Currencies.ROUBLE);

    static Banknote getBanknote(Banknote.Name name, int value) {
        return new Banknote(name, value, currency);
    }

    @Test
    @DisplayName("Create currency")
    void createCurrency() {
        currency = new AbstractCurrency("Rouble",  "\u20bd",
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

        currency = new AbstractCurrency("Rouble") {
        };

    }

    @Test
    @DisplayName("Wrong create currency")
    void createCurrencyErrors() {
        Exception e = assertThrows(IllegalArgumentException.class,
                () -> currency = new AbstractCurrency("Rouble",
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
        assertEquals("Element FIVE duplicate", e.getMessage());

        currency = new AbstractCurrency("Rouble") {
        };
    }

    @Test
    @DisplayName("Wrong create currency (null in args)")
    void createCurrencyErrorsNull() {
        Exception e = assertThrows(IllegalArgumentException.class,
                () -> currency = new AbstractCurrency("Rouble",
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

        currency = new AbstractCurrency("Rouble") {
        };
    }

    @Test
    @DisplayName("Wrong create currency (null  currency)")
    void createCurrencyErrorsNullCurrency() {
        Exception e = assertThrows(IllegalArgumentException.class,
                () -> currency = new AbstractCurrency("") {
                });

        assertEquals(null, e.getMessage());


        e = assertThrows(IllegalArgumentException.class,
                () -> currency = new AbstractCurrency(null) {
                });

        assertEquals(null, e.getMessage());
    }

    @Test
    @DisplayName("get banknote")
    void getBanknote() {
        currency = new AbstractCurrency("Rouble",
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
        Banknote expected = new Banknote(FIVE, 5, currency);
        assertEquals(true, b.equals(expected));
    }

    @Test
    @DisplayName("get banknote")
    void getBanknoteTest() {
        AbstractCurrency currency = new Rouble();

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
    @DisplayName("get all banknotes")
    void getAllBanknotes() {
        currency = new AbstractCurrency("Rouble", ONE, FIVE, HUNDRED) {
        };

        Banknote[] arr = currency.getBanknotes();
        assertEquals(3, arr.length);
        assertEquals(true, getBanknote(ONE, 1).equals(arr[0]));
        assertEquals(true, getBanknote(FIVE, 5).equals(arr[1]));
        assertEquals(true, getBanknote(HUNDRED, 100).equals(arr[2]));

        Exception e = assertThrows(IllegalArgumentException.class, () -> currency.get(FIFTY));
        assertEquals("FIFTY not found in this currency", e.getMessage());
    }
}