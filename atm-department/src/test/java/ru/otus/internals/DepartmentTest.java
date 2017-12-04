package ru.otus.internals;

import org.junit.jupiter.api.Test;
import ru.otus.currency.BanknoteName;
import ru.otus.currency.Currency;
import ru.otus.currency.CurrencyFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DepartmentTest {

    private Currency currency = CurrencyFactory.getCurrency("Rouble");
    private ATM atm = new ATMBuilder().addBanknote(currency.get(BanknoteName.FIVE_THOUSAND), 100)
            .addBanknote(currency.get(BanknoteName.TWO_THOUSAND), 100)
            .addBanknote(currency.get(BanknoteName.THOUSAND), 100)
            .addBanknote(currency.get(BanknoteName.FIVE_HUNDRED), 100)
            .build();

    @Test
    void addTest() {
        ATMDepartment department = new Department();

        department.addATM(atm);
        Exception e = assertThrows(IllegalArgumentException.class, () -> department.addATM(null));
        assertEquals("atm is null", e.getMessage());

        e = assertThrows(IllegalArgumentException.class, () -> department.addATM(atm));
        assertEquals("atm already added", e.getMessage());
    }

    @Test
    void refillATMTest() {
        ATMDepartment department = new Department();

        ATM emptyATM = new ATMBuilder().build();

        Exception e = assertThrows(IllegalArgumentException.class, () -> department.refillATM(emptyATM));
        assertEquals("atm not found", e.getMessage());

        ATM atm =  new ATMBuilder().addBanknote(currency.get(BanknoteName.FIVE_THOUSAND), 100)
                .addBanknote(currency.get(BanknoteName.TWO_THOUSAND), 100)
                .addBanknote(currency.get(BanknoteName.THOUSAND), 100)
                .addBanknote(currency.get(BanknoteName.FIVE_HUNDRED), 100)
                .addBanknote(currency.get(BanknoteName.TWO_HUNDRED), 100)
                .addBanknote(currency.get(BanknoteName.HUNDRED), 100)
                .addBanknote(currency.get(BanknoteName.FIFTY), 100)
                .addBanknote(currency.get(BanknoteName.TEN), 100)
                .addBanknote(currency.get(BanknoteName.FIVE), 100)
                .addBanknote(currency.get(BanknoteName.TWO), 100)
                .addBanknote(currency.get(BanknoteName.ONE), 100)
                .build();

        department.addATM(atm);
        long sum = atm.getBalance();
        atm.getCash(atm.getBalance());
        assertEquals(0, atm.getBalance());

        department.refillATM(atm);

        assertEquals(sum, atm.getBalance());
    }

    @Test
    void refillAllTest() {
        ATMDepartment department = new Department();

        department.addATM(atm);
        department.refillAll();
    }

    @Test
    void getAvailableCash() {
        ATMDepartment department = new Department();

        department.addATM(atm);
        assertEquals(850_000L, department.getAvailableCash());

    }

}
