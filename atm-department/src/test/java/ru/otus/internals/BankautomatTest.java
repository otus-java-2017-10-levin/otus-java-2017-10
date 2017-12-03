package ru.otus.internals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.currency.*;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BankautomatTest {

    private ATM atm;
    private final Currency cur = CurrencyFactory.getCurrency("Rouble");
    private final Banknote hundreds = cur != null ? cur.get(BanknoteName.HUNDRED) : null;

    @BeforeEach
    void createATM() {
        atm = new Bankautomat();
    }

    @Test
    @DisplayName("add banknotes")
    void TestAddBanknotes() {
        atm.addBanknote(hundreds, 100);

        assertEquals(100 * 100, atm.getBalance());
    }

    @Test
    @DisplayName("get cash")
    void TestGetCash() {
        Currency cur = CurrencyFactory.getCurrency("Rouble");
        atm.addBanknote(hundreds, 100);

        atm.getCash(5000);
        assertEquals(5000, atm.getBalance());
    }


    @Test
    @DisplayName("test currencies")
    void currenciesTest() {
        Currency cur = CurrencyFactory.getCurrency("Rouble");
        atm.addBanknote(hundreds, 100);

        atm.getCash(5000);
        assertEquals(5000, atm.getBalance());
    }

    @Test
    void saveStateTest() {
        ATM atm = new Bankautomat();
        atm.saveState();
        atm.addBanknote(cur.get(BanknoteName.FIVE_THOUSAND), 2);
        atm.saveState();
        assertEquals(10000, atm.getBalance());

        atm.loadState(Rollback.STATES.INITIAL);
        assertEquals(0, atm.getBalance());

        atm.loadState(Rollback.STATES.LAST_MODIFICATION);
        assertEquals(10000, atm.getBalance());
    }

    @Test
    void equalsTest() {
        Bankautomat b1 = new Bankautomat();
        Bankautomat b2 = new Bankautomat();

        assertEquals(true, b1.equals(b2));
        b1.addBanknote(cur.get(BanknoteName.FIVE_THOUSAND), 10);
        b2.addBanknote(cur.get(BanknoteName.FIVE_THOUSAND), 11);
        assertEquals(false, b1.equals(b2));

        b1.addBanknote(cur.get(BanknoteName.FIVE_THOUSAND), 1);
        assertEquals(true, b1.equals(b2));

        b1.addBanknote(cur.get(BanknoteName.FIFTY), 10);
        assertEquals(false, b1.equals(b2));
    }
}