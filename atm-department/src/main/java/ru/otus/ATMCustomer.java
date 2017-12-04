package ru.otus;

import org.apache.log4j.Logger;
import ru.otus.common.CommonHelper;
import ru.otus.currency.Currency;
import ru.otus.internals.ATM;

import java.util.Arrays;
import java.util.Random;

public class ATMCustomer {

    private Currency cur;
    private static int counter = 0;
    private final int ID = counter++;
    private static final Random rnd = new Random();
    private final static Logger logger = Logger.getLogger(ATMCustomer.class);

    private enum Action {
        GET(1),
        ADD(2),
        BALANCE(3);

        private final int value;

        Action(int value) {
            this.value = value;
        }

        static Action getRandomAction() {
            int count = Action.values().length-1;

            int random = rnd.nextInt(count)+1;

            Action res = Arrays.stream(Action.values()).filter(el -> el.value == random).findFirst().orElse(null);

            CommonHelper.throwIf(IllegalStateException.class, "no such element " + random, () -> res == null);
            return res;
        }
    }

    ATMCustomer(Currency cur) {
        this.cur = cur;
    }

    public void work(ATM atm) {
        Action act = Action.getRandomAction();

        switch (act) {
            case GET:
                getCash(atm);
                break;
            default:
                logger.info("Skipping Customer (id:" +  ID + ")");
        }
    }

    private void getCash(ATM atm) {
        long cash = rnd.nextInt(100_000);
        logger.info("Customer (id: "+ ID +") gets " + cur.formatCurrency(cash) + " from ATM (" + atm.getId()+ ")");
        try {
            atm.getCash(cash);
        } catch (RuntimeException e) {
            logger.info(e.getMessage());
        }
    }
}
