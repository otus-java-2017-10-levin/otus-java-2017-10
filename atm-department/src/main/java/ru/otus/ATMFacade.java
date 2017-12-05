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
import java.util.List;

class ATMFacade {

    private final Logger logger = LogManager.getLogger(ATMFacade.class);
    private static List<ATM> atms = new ArrayList<>();
    private final Currency currency = CurrencyFactory.getCurrency(CurrencyFactory.Currencies.ROUBLE);
    private ATMDepartment department = DepartmentFactory.create();
    private CustomerPool pool = new CustomerPool(currency);

    public void createAtm(int numbers) {
        try {
            for (int i = 0; i < numbers; i++) {
                ATM atm = createATM();
                department.addATM(atm);
                atms.add(atm);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void start(int waitTime, int customerPerAtm) {
        if (customerPerAtm > 0)
            for (int i=0; i < customerPerAtm; i++)
                atms.forEach(pool::addATM);
        else {
            logger.info("customerPerAtm < 0. Set 1 customer per ATM.");
            atms.forEach(pool::addATM);
        }

        while (true) {
            departmentWork(department);

            try {
                Thread.sleep(waitTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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

    private void departmentWork(ATMDepartment department) {
        logger.info("Available cash: " + currency.formatCurrency(department.getAvailableCash()));
        department.refillAll();
    }

}
