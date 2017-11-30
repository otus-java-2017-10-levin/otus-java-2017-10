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
    public long handle(long sum, Map<Banknote, Integer> atmCash, Map<Banknote, Integer> result) {
        long ret = calculate(sum, atmCash, result);
//            if (sum == ret)
//                return 0;
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

    private long calculate(long sum, Map<Banknote, Integer> atmCash, Map<Banknote, Integer> result) {
        if (sum < 0 || atmCash == null || result == null)
            throw new IllegalArgumentException();

        int totalBanknotes = 0;
        if (atmCash.containsKey(banknoteHandler))
            totalBanknotes = atmCash.get(banknoteHandler);


        if (totalBanknotes < 0)
            throw new IllegalArgumentException();

        if (result.containsValue(banknoteHandler)) {
            throw new IllegalArgumentException();
        }

        int banknotePrice = banknoteHandler.getValue();
        int res = (int)(sum / banknotePrice);

        if (res > totalBanknotes)
            res = totalBanknotes;

        result.put(banknoteHandler, res);
        return res*banknotePrice;
    }
}
