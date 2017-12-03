package ru.otus.internals;

import ru.otus.common.CommonHelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class Department implements ATMDepartment {

    private final double refillPercent = 0.20;
    private final Set<ATMInfo> atms = new HashSet<>();

    Department() {    }

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

        double coeff = atm.getBalance() / atmInfo.getInitialCash();
        if (coeff < refillPercent) {
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

        private List<Memento<ATM>> mementoList = new ArrayList<>();

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
