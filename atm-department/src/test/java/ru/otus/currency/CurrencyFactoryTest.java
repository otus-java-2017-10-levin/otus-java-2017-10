package ru.otus.currency;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.otus.currency.Banknote.Name.*;
/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
class CurrencyFactoryTest {
    @Test
    void createCurrency() {
        Currency cur = CurrencyFactory.getCurrency(CurrencyFactory.Currencies.ROUBLE);

        assert cur != null;
        Banknote[] banknotes = cur.getBanknotes();

        assertEquals(true, banknotes != null);
        assert banknotes != null;
        assertEquals(11, banknotes.length);
        assertEquals(CurrencyTest.getBanknote(ONE, 1), banknotes[0]);
        assertEquals(true, CurrencyTest.getBanknote(TWO, 2).equals(banknotes[1]));
        assertEquals(true, CurrencyTest.getBanknote(FIVE, 5).equals(banknotes[2]));
        assertEquals(true, CurrencyTest.getBanknote(TEN, 10).equals(banknotes[3]));
        assertEquals(true, CurrencyTest.getBanknote(FIFTY, 50).equals(banknotes[4]));
        assertEquals(true, CurrencyTest.getBanknote(HUNDRED, 100).equals(banknotes[5]));
        assertEquals(true, CurrencyTest.getBanknote(TWO_HUNDRED, 200).equals(banknotes[6]));
        assertEquals(true, CurrencyTest.getBanknote(FIVE_HUNDRED, 500).equals(banknotes[7]));
        assertEquals(true, CurrencyTest.getBanknote(THOUSAND, 1000).equals(banknotes[8]));
        assertEquals(true, CurrencyTest.getBanknote(TWO_THOUSAND, 2000).equals(banknotes[9]));
        assertEquals(true, CurrencyTest.getBanknote(FIVE_THOUSAND, 5000).equals(banknotes[10]));
        assertEquals("Rouble", cur.getCurrencyName());
    }

    @Test
    @DisplayName("exception while creation")
    void createCurrencyExceptions() {
        Exception e = assertThrows(IllegalArgumentException.class, () -> CurrencyFactory.getCurrency(null));
        assertEquals(null, e.getMessage());
    }

}