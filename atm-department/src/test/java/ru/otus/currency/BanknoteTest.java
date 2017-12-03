package ru.otus.currency;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


class BanknoteTest {

    private final Currency currency = CurrencyFactory.getCurrency("Rouble");
    private final Currency dollarsCurrency = CurrencyFactory.getCurrency("Dollar");

    @Test
    @DisplayName("Banknote constructor")
    void BanknoteConstructorTest() {
        int value = 100;
        Banknote note = new Banknote(BanknoteName.HUNDRED, value, currency);

        assertEquals(BanknoteName.HUNDRED, note.getName());
        assertEquals(value, note.getValue());
        assert currency != null;
        assertEquals(currency.getCurrencyName(), note.getCurrencyName());
    }

    @Test
    void getValueTest() {
        int value = 100;
        Banknote note = new Banknote(BanknoteName.HUNDRED, value, currency);

        assertEquals(value, note.getValue());
    }

    @Test
    void getSignTest() {
        int value = 100;
        Banknote note = new Banknote(BanknoteName.HUNDRED, value, dollarsCurrency);

        assertEquals("$", note.getSign());
    }

    @Test
    @DisplayName("equals")
    void equalsTest() {
        Banknote a = new Banknote(BanknoteName.FIFTY, 50, currency);
        Banknote b = new Banknote(BanknoteName.FIFTY, 50, currency);
        Banknote c = new Banknote(BanknoteName.FIFTY, 0, currency);
        Banknote d = new Banknote(BanknoteName.TWENTY, 50, currency);
        Banknote e = new Banknote(BanknoteName.FIFTY, 50, dollarsCurrency);

        assertEquals(true, a.equals(b));
        assertEquals(false, a.equals(c));
        assertEquals(false, a.equals(d));
        assertEquals(false, a.equals(e));
    }

    @Test
    @DisplayName("hashCode")
    void hashCodeTest() {
        Banknote a = new Banknote(BanknoteName.FIFTY, 50, currency);
        Banknote b = new Banknote(BanknoteName.FIFTY, 50, currency);
        Banknote c = new Banknote(BanknoteName.FIFTY, 0, currency);
        Banknote d = new Banknote(BanknoteName.TWENTY, 50, currency);
        Banknote e = new Banknote(BanknoteName.FIFTY, 50, dollarsCurrency);

        assertEquals(true, a.hashCode()==b.hashCode());
        assertEquals(false, a.hashCode()==c.hashCode());
        assertEquals(false, a.hashCode()==d.hashCode());
        assertEquals(false, a.hashCode()==e.hashCode());
    }
}