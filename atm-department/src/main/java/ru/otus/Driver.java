package ru.otus;

import org.apache.log4j.Logger;
import ru.otus.currency.BanknoteName;
import ru.otus.currency.Currency;
import ru.otus.currency.CurrencyFactory;
import ru.otus.internals.ATM;
import ru.otus.internals.ATMBuilder;
import ru.otus.internals.ATMDepartment;
import ru.otus.internals.DepartmentFactory;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("IfCanBeSwitch")
class Driver {

    private static final Logger logger = Logger.getLogger(Driver.class);
    private static List<ATM> atms = new ArrayList<>();
    private Currency currency = CurrencyFactory.getCurrency(CurrencyFactory.Currencies.ROUBLE);

    public static void main(String[] args) {
        Driver driver = new Driver();

        driver.run();
    }

    private void run() {
        ATMDepartment department = DepartmentFactory.create();
        try {
            for (int i=0; i < 2; i++) {
                department.addATM(createATM());
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        logger.info("Available cash: " +currency.formatCurrency(department.getAvailableCash()));

        while (true) {
            atms.forEach(this::worker);
            departmentWork(department);

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    private void departmentWork(ATMDepartment department) {
       logger.info("Available cash: " +currency.formatCurrency(department.getAvailableCash()));
        department.refillAll();
    }

    private void worker(ATM atm) {
        ATMCustomer customer = new ATMCustomer(currency);

        customer.work(atm);
    }

    private ATM createATM() {
        ATM atm =  new ATMBuilder().addBanknote(currency.get(BanknoteName.FIVE_THOUSAND), 10)
                .addBanknote(currency.get(BanknoteName.TWO_THOUSAND), 10)
                .addBanknote(currency.get(BanknoteName.THOUSAND), 10)
                .addBanknote(currency.get(BanknoteName.FIVE_HUNDRED), 10)
                .addBanknote(currency.get(BanknoteName.TWO_HUNDRED), 10)
                .addBanknote(currency.get(BanknoteName.HUNDRED), 10)
                .addBanknote(currency.get(BanknoteName.FIFTY), 10)
                .addBanknote(currency.get(BanknoteName.TEN), 10)
                .addBanknote(currency.get(BanknoteName.FIVE), 10)
                .addBanknote(currency.get(BanknoteName.TWO), 10)
                .addBanknote(currency.get(BanknoteName.ONE), 10)
                .build();

        atms.add(atm);
        return atm;
    }
}