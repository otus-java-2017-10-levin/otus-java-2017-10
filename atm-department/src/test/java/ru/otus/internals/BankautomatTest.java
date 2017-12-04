package ru.otus.internals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.currency.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BankautomatTest {

    private ATM atm;
    private final Currency cur = CurrencyFactory.getCurrency(CurrencyFactory.Currencies.ROUBLE);
    private final Banknote hundreds = cur != null ? cur.get(Banknote.Name.HUNDRED) : null;

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
        Currency cur = CurrencyFactory.getCurrency(CurrencyFactory.Currencies.ROUBLE);
        atm.addBanknote(hundreds, 100);

        atm.getCash(5000);
        assertEquals(5000, atm.getBalance());
    }


    @Test
    @DisplayName("test currencies")
    void currenciesTest() {
        Currency cur = CurrencyFactory.getCurrency(CurrencyFactory.Currencies.ROUBLE);
        atm.addBanknote(hundreds, 100);

        atm.getCash(5000);
        assertEquals(5000, atm.getBalance());
    }

    @Test
    void saveStateTest() {
        ATM atm = new Bankautomat();
        atm.saveState();
        atm.addBanknote(cur.get(Banknote.Name.FIVE_THOUSAND), 2);
        atm.saveState();
        assertEquals(10000, atm.getBalance());

        atm.loadState(Rollback.STATES.INITIAL);
        assertEquals(0, atm.getBalance());
    }

    @Test
    void equalsTest() {
        Bankautomat b1 = new Bankautomat();
        Bankautomat b2 = new Bankautomat();

        assertEquals(false, b1.equals(b2));
    }
}