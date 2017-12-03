package ru.otus.currency;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


class BanknoteNameTest {

    @Test
    void getBanknoteTest() {
        assertEquals(BanknoteName.ONE, BanknoteName.get(1));
        assertEquals(BanknoteName.TWO, BanknoteName.get(2));
        assertEquals(BanknoteName.FIVE, BanknoteName.get(5));
        assertEquals(BanknoteName.TEN, BanknoteName.get(10));
        assertEquals(BanknoteName.TWENTY, BanknoteName.get(20));
        assertEquals(BanknoteName.FIFTY, BanknoteName.get(50));
        assertEquals(BanknoteName.HUNDRED, BanknoteName.get(100));
        assertEquals(BanknoteName.TWO_HUNDRED, BanknoteName.get(200));
        assertEquals(BanknoteName.FIVE_HUNDRED, BanknoteName.get(500));
        assertEquals(BanknoteName.THOUSAND, BanknoteName.get(1000));
        assertEquals(BanknoteName.TWO_THOUSAND, BanknoteName.get(2000));
        assertEquals(BanknoteName.FIVE_THOUSAND, BanknoteName.get(5000));
    }

}
