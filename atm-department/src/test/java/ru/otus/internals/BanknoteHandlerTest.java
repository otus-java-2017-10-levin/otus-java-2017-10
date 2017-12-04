package ru.otus.internals;


import org.junit.jupiter.api.Test;
import ru.otus.currency.Banknote;
import ru.otus.currency.Currency;
import ru.otus.currency.CurrencyFactory;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BanknoteHandlerTest {

    private final Currency cur = CurrencyFactory.getCurrency(CurrencyFactory.Currencies.ROUBLE);
    @Test
    void creationTest() {

        assert cur != null;

        Handler h = new BanknoteHandler(cur.get(Banknote.Name.HUNDRED));

        Exception e = assertThrows(IllegalArgumentException.class, () -> new BanknoteHandler(null));
        assertEquals(null, e.getMessage());
    }

    @Test
    void handleTest() {
        assert cur != null;
        Handler h = new BanknoteHandler(cur.get(Banknote.Name.FIVE_THOUSAND));
        Map<Banknote, Integer> atmCash = new HashMap<>();
        Map<Banknote, Integer> result = new HashMap<>();
        atmCash.put(cur.get(Banknote.Name.FIVE_THOUSAND), 100);

        assertEquals(400000, h.handle(400300, atmCash, result));
        assertEquals(1, result.size());
        assertEquals(80, (long)result.get(cur.get(Banknote.Name.FIVE_THOUSAND)));

        Exception e = assertThrows(IllegalArgumentException.class, () -> h.handle(-50000, atmCash, result));
        assertEquals(null, e.getMessage());

        atmCash.put(cur.get(Banknote.Name.FIVE_THOUSAND), -1);
        e = assertThrows(IllegalArgumentException.class, () -> h.handle(500300, atmCash, result));
        assertEquals(null, e.getMessage());

        atmCash.put(cur.get(Banknote.Name.FIVE_THOUSAND), 0);
        Map<Banknote, Integer> result1 = new HashMap<>();
        assertEquals(0, h.handle(10000, atmCash, result1));
        assertEquals(0, result1.size());
        assertEquals(0, (long)result1.getOrDefault(cur.get(Banknote.Name.FIVE_THOUSAND), 0));

    }

    @Test
    void handleTestExp() {
        assert cur != null;
        Handler h = new BanknoteHandler(cur.get(Banknote.Name.FIVE_THOUSAND));
        h.setNext(new BanknoteHandler(cur.get(Banknote.Name.HUNDRED)));

        Map<Banknote, Integer> atmCash = new HashMap<>();
        Map<Banknote, Integer> result = new HashMap<>();
        atmCash.put(cur.get(Banknote.Name.FIVE_THOUSAND), 100);
        atmCash.put(cur.get(Banknote.Name.HUNDRED), 100);

        long ret = h.handle(10500, atmCash, result);
        assertEquals(10500L, ret);

    }
}