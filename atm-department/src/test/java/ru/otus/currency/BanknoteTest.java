package ru.otus.currency;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


class BanknoteTest {

    private final Currency currency = CurrencyFactory.getCurrency(CurrencyFactory.Currencies.ROUBLE);

    @Test
    @DisplayName("Banknote constructor")
    void BanknoteConstructorTest() {
        int value = 100;
        Banknote note = Banknote.of(Banknote.Name.HUNDRED, value, currency);

        assertEquals(Banknote.Name.HUNDRED, note.getName());
        assertEquals(value, note.getValue());
        assert currency != null;
        assertEquals(currency.getCurrencyName(), note.getCurrencyName());
    }

    @Test
    void getValueTest() {
        int value = 100;
        Banknote note = Banknote.of(Banknote.Name.HUNDRED, value, currency);

        assertEquals(value, note.getValue());
    }

    @Test
    void getSignTest() {
        int value = 100;
        Banknote note = Banknote.of(Banknote.Name.HUNDRED, value, currency);

        assertEquals("\u20bd", note.getSign());
    }

    @Test
    @DisplayName("equals")
    void equalsTest() {
        Banknote a = Banknote.of(Banknote.Name.FIFTY, 50, currency);
        Banknote b = Banknote.of(Banknote.Name.FIFTY, 50, currency);
        Banknote c = Banknote.of(Banknote.Name.FIFTY, 0, currency);
        Banknote d = Banknote.of(Banknote.Name.TWENTY, 50, currency);

        assertEquals(true, a.equals(b));
        assertEquals(false, a.equals(c));
        assertEquals(false, a.equals(d));
    }

    @Test
    @DisplayName("hashCode")
    void hashCodeTest() {
        Banknote a = Banknote.of(Banknote.Name.FIFTY, 50, currency);
        Banknote b = Banknote.of(Banknote.Name.FIFTY, 50, currency);
        Banknote c = Banknote.of(Banknote.Name.FIFTY, 0, currency);
        Banknote d = Banknote.of(Banknote.Name.TWENTY, 50, currency);

        assertEquals(true, a.hashCode()==b.hashCode());
        assertEquals(false, a.hashCode()==c.hashCode());
        assertEquals(false, a.hashCode()==d.hashCode());
    }
}