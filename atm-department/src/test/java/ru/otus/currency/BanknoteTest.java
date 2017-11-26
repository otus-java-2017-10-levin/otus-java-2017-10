package ru.otus.currency;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.currency.Banknote;
import ru.otus.currency.BanknoteNames;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class BanknoteTest {


    @Test
    @DisplayName("Banknote constructor")
    public void BanknoteTest() {
        String currency = "Ruble";
        long value = 100;
        Banknote note = new Banknote(BanknoteNames.HUNDRED, value, currency);

        assertEquals(BanknoteNames.HUNDRED, note.getName());
        assertEquals(value, note.getValue());
        assertEquals(currency, note.getCurrencyName());
    }

    @Test
    @DisplayName("equals")
    public void equalsTest() {
        Banknote a = new Banknote(BanknoteNames.FIFTY, 50, "Ruble");
        Banknote b = new Banknote(BanknoteNames.FIFTY, 50, "Ruble");
        Banknote c = new Banknote(BanknoteNames.FIFTY, 0, "Ruble");
        Banknote d = new Banknote(BanknoteNames.TWENTY, 50, "Ruble");
        Banknote e = new Banknote(BanknoteNames.FIFTY, 50, "Dollar");

        assertEquals(true, a.equals(b));
        assertEquals(false, a.equals(c));
        assertEquals(false, a.equals(d));
        assertEquals(false, a.equals(e));
    }

    @Test
    @DisplayName("hashCode")
    public void hashCodeTest() {
        Banknote a = new Banknote(BanknoteNames.FIFTY, 50, "Ruble");
        Banknote b = new Banknote(BanknoteNames.FIFTY, 50, "Ruble");
        Banknote c = new Banknote(BanknoteNames.FIFTY, 0, "Ruble");
        Banknote d = new Banknote(BanknoteNames.TWENTY, 50, "Ruble");
        Banknote e = new Banknote(BanknoteNames.FIFTY, 50, "Dollar");

        assertEquals(true, a.hashCode()==b.hashCode());
        assertEquals(false, a.hashCode()==c.hashCode());
        assertEquals(false, a.hashCode()==d.hashCode());
        assertEquals(false, a.hashCode()==e.hashCode());
    }
}