package ru.otus.internals;

import ru.otus.common.Utils;
import ru.otus.currency.Banknote;

import java.util.Map;

class BanknoteHandler implements Handler {

    BanknoteHandler(Banknote banknote) {

        Utils.throwIf(IllegalArgumentException.class, null, () -> banknote == null);

        banknoteHandler = banknote;
    }

    @Override
    public long handle(long sum, Map<Banknote, Integer> atmCash, Map<Banknote, Integer> result) {
        long ret = calculate(sum, atmCash, result);
        if (nextHandler != null) {
            ret += nextHandler.handle(sum - ret, atmCash, result);
        }
        return ret;
    }

    @Override
    public void setNext(Handler handler) {
        if (nextHandler == null)
            nextHandler = handler;
        else
            nextHandler.setNext(handler);
    }

    private final Banknote banknoteHandler;
    private Handler nextHandler;

    private long calculate(long sum, Map<Banknote, Integer> atmCash, Map<Banknote, Integer> result) {
        Utils.throwIf(IllegalArgumentException.class, "wrong arguments",
                () -> sum < 0 || atmCash == null || result == null);

        Utils.throwIf(IllegalArgumentException.class, "illegal banknotes in ATM!",
                () -> atmCash.getOrDefault(banknoteHandler, 0) < 0);

        Utils.throwIf(IllegalStateException.class,
                "Result already contains banknote (" +banknoteHandler+ ")",
                () -> result.containsKey(banknoteHandler));

        int totalBanknotes = atmCash.getOrDefault(banknoteHandler, 0);

        int banknotePrice = banknoteHandler.getValue();
        int res = (int)(sum / banknotePrice);

        if (res > totalBanknotes)
            res = totalBanknotes;

        if (res != 0) {
            result.put(banknoteHandler, res);
        }
        return res*banknotePrice;
    }
}