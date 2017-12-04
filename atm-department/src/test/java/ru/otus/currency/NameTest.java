package ru.otus.currency;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


class NameTest {

    @Test
    void getBanknoteTest() {
        assertEquals(Banknote.Name.ONE, Banknote.Name.get(1));
        assertEquals(Banknote.Name.TWO, Banknote.Name.get(2));
        assertEquals(Banknote.Name.FIVE, Banknote.Name.get(5));
        assertEquals(Banknote.Name.TEN, Banknote.Name.get(10));
        assertEquals(Banknote.Name.TWENTY, Banknote.Name.get(20));
        assertEquals(Banknote.Name.FIFTY, Banknote.Name.get(50));
        assertEquals(Banknote.Name.HUNDRED, Banknote.Name.get(100));
        assertEquals(Banknote.Name.TWO_HUNDRED, Banknote.Name.get(200));
        assertEquals(Banknote.Name.FIVE_HUNDRED, Banknote.Name.get(500));
        assertEquals(Banknote.Name.THOUSAND, Banknote.Name.get(1000));
        assertEquals(Banknote.Name.TWO_THOUSAND, Banknote.Name.get(2000));
        assertEquals(Banknote.Name.FIVE_THOUSAND, Banknote.Name.get(5000));
    }

}
