package ru.otus.internals;


import org.junit.jupiter.api.Test;
import ru.otus.currency.Banknote;
import ru.otus.currency.BanknoteNames;
import ru.otus.currency.Currency;
import ru.otus.currency.CurrencyHelper;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BanknoteHandlerTest {

    private final Currency cur = CurrencyHelper.createCurrency("ruble");
    @Test
    void creationTest() {

        assert cur != null;
        Handler h = new BanknoteHandler(cur.get(BanknoteNames.HUNDRED));

        Exception e = assertThrows(IllegalArgumentException.class, () -> new BanknoteHandler(null));
        assertEquals(null, e.getMessage());
    }

    @Test
    void handleTest() {
        assert cur != null;
        Handler h = new BanknoteHandler(cur.get(BanknoteNames.FIVE_THOUSAND));
        Map<Banknote, Integer> atmCash = new HashMap<>();
        Map<Banknote, Integer> result = new HashMap<>();
        atmCash.put(cur.get(BanknoteNames.FIVE_THOUSAND), 100);

        assertEquals(400000, h.handle(400300, atmCash, result));
        assertEquals(1, result.size());
        assertEquals(80, (long)result.get(cur.get(BanknoteNames.FIVE_THOUSAND)));

        Exception e = assertThrows(IllegalArgumentException.class, () -> h.handle(-50000, atmCash, result));
        assertEquals(null, e.getMessage());

        atmCash.put(cur.get(BanknoteNames.FIVE_THOUSAND), -1);
        e = assertThrows(IllegalArgumentException.class, () -> h.handle(500300, atmCash, result));
        assertEquals(null, e.getMessage());

        atmCash.put(cur.get(BanknoteNames.FIVE_THOUSAND), 0);
        assertEquals(0, h.handle(10000, atmCash, result));
    }

    @Test
    void handleTestExp() {
        assert cur != null;
        Handler h = new BanknoteHandler(cur.get(BanknoteNames.FIVE_THOUSAND));
        h.setNext(new BanknoteHandler(cur.get(BanknoteNames.HUNDRED)));

        Map<Banknote, Integer> atmCash = new HashMap<>();
        Map<Banknote, Integer> result = new HashMap<>();
        atmCash.put(cur.get(BanknoteNames.FIVE_THOUSAND), 100);
        atmCash.put(cur.get(BanknoteNames.HUNDRED), 100);

        long ret = h.handle(10500, atmCash, result);
        assertEquals(10500L, ret);

    }
}