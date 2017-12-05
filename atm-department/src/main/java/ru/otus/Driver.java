package ru.otus;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.otus.currency.Banknote;
import ru.otus.currency.Currency;
import ru.otus.currency.CurrencyFactory;
import ru.otus.internals.ATM;
import ru.otus.internals.ATMBuilder;
import ru.otus.internals.ATMDepartment;
import ru.otus.internals.DepartmentFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("IfCanBeSwitch")
class Driver {

    private static final Logger logger = LogManager.getLogger(Driver.class);
    private static List<ATM> atms = new ArrayList<>();
    private Currency currency = CurrencyFactory.getCurrency(CurrencyFactory.Currencies.ROUBLE);

    public static void main(String[] args) {
        new Driver().run();
    }

    private void run() {
        ATMDepartment department = DepartmentFactory.create();
        CustomerPool pool = new CustomerPool(currency);

        try {
            for (int i = 0; i < 1; i++) {
                ATM atm = createATM();
                department.addATM(atm);
                atms.add(atm);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        atms = Collections.synchronizedList(atms);
        for (int i = 0; i < 5; i++) {
            atms.forEach(pool::addATM);
        }

        while (true) {
            departmentWork(department);

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void departmentWork(ATMDepartment department) {
        logger.info("Available cash: " + currency.formatCurrency(department.getAvailableCash()));
        department.refillAll();
    }

    private ATM createATM() {
        return new ATMBuilder().addBanknote(currency.get(Banknote.Name.FIVE_THOUSAND), 10)
                .addBanknote(currency.get(Banknote.Name.TWO_THOUSAND), 10)
                .addBanknote(currency.get(Banknote.Name.THOUSAND), 10)
                .addBanknote(currency.get(Banknote.Name.FIVE_HUNDRED), 10)
                .addBanknote(currency.get(Banknote.Name.TWO_HUNDRED), 100)
                .addBanknote(currency.get(Banknote.Name.HUNDRED), 200)
                .addBanknote(currency.get(Banknote.Name.FIFTY), 300)
                .addBanknote(currency.get(Banknote.Name.TEN), 400)
                .addBanknote(currency.get(Banknote.Name.FIVE), 500)
                .addBanknote(currency.get(Banknote.Name.TWO), 600)
                .addBanknote(currency.get(Banknote.Name.ONE), 1200)
                .build();
    }
}