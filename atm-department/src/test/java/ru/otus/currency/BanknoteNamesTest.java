package ru.otus.currency;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


class BanknoteNamesTest {

    @Test
    void getBanknoteTest() {
        assertEquals(BanknoteNames.ONE, BanknoteNames.get(1));
        assertEquals(BanknoteNames.TWO, BanknoteNames.get(2));
        assertEquals(BanknoteNames.FIVE, BanknoteNames.get(5));
        assertEquals(BanknoteNames.TEN, BanknoteNames.get(10));
        assertEquals(BanknoteNames.TWENTY, BanknoteNames.get(20));
        assertEquals(BanknoteNames.FIFTY, BanknoteNames.get(50));
        assertEquals(BanknoteNames.HUNDRED, BanknoteNames.get(100));
        assertEquals(BanknoteNames.TWO_HUNDRED, BanknoteNames.get(200));
        assertEquals(BanknoteNames.FIVE_HUNDRED, BanknoteNames.get(500));
        assertEquals(BanknoteNames.THOUSAND, BanknoteNames.get(1000));
        assertEquals(BanknoteNames.TWO_THOUSAND, BanknoteNames.get(2000));
        assertEquals(BanknoteNames.FIVE_THOUSAND, BanknoteNames.get(5000));
    }

}
