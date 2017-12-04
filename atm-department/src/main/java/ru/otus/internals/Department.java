package ru.otus.internals;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import ru.otus.common.CommonHelper;

import java.util.HashSet;
import java.util.Set;

class Department implements ATMDepartment {

    private final Logger logger = Logger.getLogger(Department.class);
    private final Set<ATMInfo> atms = new HashSet<>();

    Department() {
        BasicConfigurator.configure();
    }

    @Override
    public void addATM(ATM atm) {
        CommonHelper.throwIf(IllegalArgumentException.class, "atm is null",
                () -> atm == null);
        atm.saveState();

        CommonHelper.throwIf(IllegalArgumentException.class, "atm already added",
                () -> atms.contains(getATM(atm)));

        atms.add(new ATMInfo(atm, atm.getBalance()));
    }

    @Override
    public void refillATM(ATM atm) throws IllegalArgumentException {
        ATMInfo atmInfo = getATM(atm);
        CommonHelper.throwIf(IllegalArgumentException.class, "atm not found", () -> atmInfo == null);

        double refillPercent = 0.20;
        double coeff = (double)atm.getBalance() / atmInfo.getInitialCash();

        if (coeff < refillPercent) {
            logger.info("refill ATM (id:" + atm.getId() + ")");
            atm.loadState(Rollback.STATES.INITIAL);
        }
    }

    @Override
    public void refillAll() {
        atms.forEach((atmInfo -> refillATM(atmInfo.getAtm())));
    }

    @Override
    public long getAvailableCash() {
        long balance = 0;
        for (ATMInfo atm : atms) {
            balance += atm.getAtm().getBalance();
        }
        return balance;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    private ATMInfo getATM(ATM atm) {
        return atms.stream().filter(e -> e.getAtm().equals(atm)).findFirst().orElse(null);
    }

    private class ATMInfo {
        private ATM atm;
        private long initialCash;

        ATMInfo(ATM atm, long initialCash) {
            this.atm = atm;
            this.initialCash = initialCash;
        }

        public ATM getAtm() {
            return atm;
        }

        long getInitialCash() {
            return initialCash;
        }
    }
}
