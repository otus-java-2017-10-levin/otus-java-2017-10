package ru.otus;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.otus.common.CommonHelper;
import ru.otus.currency.Currency;
import ru.otus.internals.ATM;

import java.util.Random;

public class ATMCustomer {

    private final Currency cur;
    private static int counter = 0;
    private final int ID = counter++;
    private static final Random rnd = new Random();
    private final static Logger logger = LogManager.getLogger(ATMCustomer.class);

    ATMCustomer(Currency cur) {
        this.cur = cur;
    }

    public void work(ATM atm) {
        CommonHelper.throwIf(IllegalArgumentException.class, "ATM - null", () -> atm == null);
        synchronized (atm) {
            getCash(atm);
        }
    }

    private void getCash(ATM atm) {
        long cash = rnd.nextInt(10_000)/10 * 10+5000;
        StringBuilder sb = new StringBuilder(Thread.currentThread().getName())
                .append(" Customer (id: ").append(ID).append(") gets ").append(cur.formatCurrency(cash))
                .append(" from ATM (").append(atm.getId()).append("; balance: ").append(cur.formatCurrency(atm.getBalance())).append(")");
        try {
            atm.getCash(cash);
            sb.append(" >> success");
            sb.append(" balance after transaction: ").append(atm.getBalance());
        } catch (RuntimeException e) {
            sb.append(" >> failed: ").append(e.getMessage());
        }
        logger.info(sb);
    }
}
