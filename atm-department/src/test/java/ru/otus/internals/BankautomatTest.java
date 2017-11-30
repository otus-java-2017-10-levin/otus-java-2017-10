package ru.otus.internals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.currency.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BankautomatTest {

    private ATM atm;
    private final Currency cur = CurrencyHelper.createCurrency("Ruble");
    private final Banknote hundreds = cur != null ? cur.get(BanknoteNames.HUNDRED) : null;
    @BeforeEach
    void createATM() {
        atm = new Bankautomat();
    }

    @Test
    @DisplayName("add banknotes")
    void TestAddBanknotes() {
        atm.addBanknote(hundreds, 100);

        assertEquals(100*100, atm.getBalance());
    }

    @Test
    @DisplayName("get cash")
    void TestGetCash() {
        Currency cur = CurrencyHelper.createCurrency("Ruble");
        atm.addBanknote(hundreds, 100);

        atm.getCash(5000, cur);
        assertEquals(5000, atm.getBalance());
    }
}