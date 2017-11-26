package ru.otus.currency;

class Ruble extends Currency {
    Ruble() {
        super("Ruble", BanknoteNames.ONE,
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