package ru.otus.internals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.currency.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BankautomatTest {

    private ATM atm;
    private Currency cur = CurrencyHelper.createCurrency("Ruble");
    private Banknote hundreds = cur.get(BanknoteNames.HUNDRED);
    @BeforeEach
    public void createATM() {
        atm = new Bankautomat();
    }

    @Test
    @DisplayName("add banknotes")
    public void TestAddBanknotes() {
        atm.addBanknote(hundreds, 100);

        assertEquals(100*100, atm.getBalance());
    }

    @Test
    @DisplayName("get cash")
    public void TestGetCash() {
        Currency cur = CurrencyHelper.createCurrency("Ruble");
        atm.addBanknote(hundreds, 100);

        atm.getCash(5000, cur);
        assertEquals(5000, atm.getBalance());
    }
}