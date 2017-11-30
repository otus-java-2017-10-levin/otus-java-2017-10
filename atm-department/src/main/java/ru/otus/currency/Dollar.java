package ru.otus.currency;


final class Dollar extends Currency {
    Dollar() {
        super("Dollar",  "$",
                BanknoteNames.ONE,
                BanknoteNames.TWO,
                BanknoteNames.FIVE,
                BanknoteNames.TEN,
                BanknoteNames.FIFTY,
                BanknoteNames.HUNDRED);
    }
}