package ru.otus.currency;

final class Rouble extends AbstractCurrency {
    Rouble() {
        super("Rouble",  "\u20bd",   Banknote.Name.ONE,
                Banknote.Name.TWO,
                Banknote.Name.FIVE,
                Banknote.Name.TEN,
                Banknote.Name.FIFTY,
                Banknote.Name.HUNDRED,
                Banknote.Name.TWO_HUNDRED,
                Banknote.Name.FIVE_HUNDRED,
                Banknote.Name.THOUSAND,
                Banknote.Name.TWO_THOUSAND,
                Banknote.Name.FIVE_THOUSAND);
    }
}