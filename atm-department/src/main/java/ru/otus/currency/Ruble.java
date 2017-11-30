package ru.otus.currency;

final class Ruble extends Currency {
    Ruble() {
        super("Ruble",  "\u20bd",   BanknoteNames.ONE,
                BanknoteNames.TWO,
                BanknoteNames.FIVE,
                BanknoteNames.TEN,
                BanknoteNames.FIFTY,
                BanknoteNames.HUNDRED,
                BanknoteNames.TWO_HUNDRED,
                BanknoteNames.FIVE_HUNDRED,
                BanknoteNames.THOUSAND,
                BanknoteNames.TWO_THOUSAND,
                BanknoteNames.FIVE_THOUSAND);
    }
}