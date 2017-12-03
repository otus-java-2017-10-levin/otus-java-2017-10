package ru.otus.internals;

import ru.otus.common.CommonHelper;

import java.util.ArrayList;
import java.util.List;

class ATMTerminals implements ATMDepartment {

    private double refillPercent = 20.0/100.0;
    private List<ATM> atms = new ArrayList<>();

    ATMTerminals() {}

    ATMTerminals(double refillPercent) {
        this.refillPercent = refillPercent;
    }

    @Override
    public void addATM(ATM atm) {
        CommonHelper.verify(IllegalArgumentException.class, "atm is null", () -> atm == null);
        CommonHelper.verify(IllegalArgumentException.class, "atm already added", () -> atms.contains(atm));
    }

    @Override
    public void refillATM(ATM atm) {
        throw new IllegalStateException("not implement");
    }

    @Override
    public void refillAll() {
        throw new IllegalStateException("not implement");
    }

    @Override
    public long getAvailableCash() {
        return 0;
    }
}
