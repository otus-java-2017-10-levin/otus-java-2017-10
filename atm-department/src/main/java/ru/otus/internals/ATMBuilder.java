package ru.otus.internals;

import ru.otus.currency.Banknote;

public class ATMBuilder {
    private ATM object;

    private ATM getObject() {
        if (object != null) {
            return object;
        }
        object = new Bankautomat();
        return object;
    }

    public ATMBuilder addBanknote(Banknote note, int count) {

        getObject().addBanknote(note, count);
        return this;
    }

    public ATM build() {
        return object;
    }
}
