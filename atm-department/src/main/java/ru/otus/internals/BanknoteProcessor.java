package ru.otus.internals;

import ru.otus.currency.Banknote;
import ru.otus.currency.Currency;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

class BanknoteProcessor {

    Handler handler;

    public boolean process(long sum, Map<Banknote, Long> atmCash, Map<Banknote, Long> result) {
        long res =  handler.handle(sum, atmCash, result);

        return res == sum;
    }

    void addHandler(Handler handler) {
        if (this.handler == null)
            this.handler = handler;
        else
            this.handler.setNext(handler);
    }

    public static BanknoteProcessor createProcessor(Currency currency) {
        List<Banknote> banknotes = Arrays.asList(currency.getBanknotes());

        banknotes.sort(Comparator.comparingLong(Banknote::getValue).reversed());
        BanknoteProcessor processor = new BanknoteProcessor();
        for (Banknote note: banknotes) {
            processor.addHandler(new BanknoteHandler(note));
        }
        return processor;
    }
}
