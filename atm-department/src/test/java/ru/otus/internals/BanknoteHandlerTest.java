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

public class BanknoteHandlerTest {

    private Currency cur = CurrencyHelper.createCurrency("ruble");
    @Test
    public void creationTest() {

        Handler h = new BanknoteHandler(cur.get(BanknoteNames.HUNDRED));
        assertEquals(true, h != null);

        Exception e = assertThrows(IllegalArgumentException.class, () -> new BanknoteHandler(null));
        assertEquals(null, e.getMessage());
    }

    @Test
    public void handleTest() {
        Handler h = new BanknoteHandler(cur.get(BanknoteNames.FIVE_THOUSAND));
        Map<Banknote, Long> atmCash = new HashMap<>();
        Map<Banknote, Long> result = new HashMap<>();
        atmCash.put(cur.get(BanknoteNames.FIVE_THOUSAND), 100L);

        assertEquals(400000, h.handle(400300, atmCash, result));
        assertEquals(1, result.size());
        assertEquals(80, (long)result.get(cur.get(BanknoteNames.FIVE_THOUSAND)));

        Exception e = assertThrows(IllegalArgumentException.class, () -> h.handle(-50000, atmCash, result));
        assertEquals(null, e.getMessage());

        atmCash.put(cur.get(BanknoteNames.FIVE_THOUSAND), -1L);
        e = assertThrows(IllegalArgumentException.class, () -> h.handle(500300, atmCash, result));
        assertEquals(null, e.getMessage());

        atmCash.put(cur.get(BanknoteNames.FIVE_THOUSAND), 0L);
        assertEquals(0, h.handle(10000, atmCash, result));
    }

    @Test
    public void handleTestExp() {
        Handler h = new BanknoteHandler(cur.get(BanknoteNames.FIVE_THOUSAND));
        h.setNext(new BanknoteHandler(cur.get(BanknoteNames.HUNDRED)));

        Map<Banknote, Long> atmCash = new HashMap<>();
        Map<Banknote, Long> result = new HashMap<>();
        atmCash.put(cur.get(BanknoteNames.FIVE_THOUSAND), 100L);
        atmCash.put(cur.get(BanknoteNames.HUNDRED), 100L);

        long ret = h.handle(10500, atmCash, result);
        assertEquals(10500L, ret);

    }
}