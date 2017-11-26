package ru.otus.internals;

import ru.otus.currency.Banknote;

import java.util.Map;

class BanknoteHandler implements Handler {

    public BanknoteHandler(Banknote banknote) {
        if (banknote == null)
            throw new IllegalArgumentException();

        banknoteHandler = banknote;
    }

    @Override
    public long handle(long sum, Map<Banknote, Long> atmCash, Map<Banknote, Long> result) {
        long ret = 0;
            ret = calculate(sum, atmCash, result);
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

    private Banknote banknoteHandler;
    private Handler nextHandler;

    private long calculate(long sum, Map<Banknote, Long> atmCash, Map<Banknote, Long> result) {
        if (sum < 0 || atmCash == null || result == null)
            throw new IllegalArgumentException();

        long max = 0;
        if (atmCash.containsKey(banknoteHandler))
            max = atmCash.get(banknoteHandler);


        if (max < 0)
            throw new IllegalArgumentException();

        if (result.containsValue(banknoteHandler)) {
            throw new IllegalArgumentException();
        }

        long banknotePrice = banknoteHandler.getValue();
        long res = sum / banknotePrice;

        if (res > max)
            res = max;

        result.put(banknoteHandler, res);
        return res*banknotePrice;
    }
}
