package ru.otus.internals;

import ru.otus.currency.Banknote;
import ru.otus.currency.Currency;

import java.util.HashMap;
import java.util.Map;

class Bankautomat implements ATM {
    Bankautomat() { banknotes = new HashMap<>(); }

    public void addBanknote(Banknote note, long count) {
        if (note == null || count < 0 ) {
            throw new IllegalArgumentException();
        }

        banknotes.put(note, count);
    }

    public void getCash(long value, Currency currency) throws RuntimeException {

        if (value > getBalance()) {
            throw new RuntimeException("Insufficient funds");
        }
        BanknoteProcessor processor = BanknoteProcessor.createProcessor(currency);

        Map<Banknote, Long> result = new HashMap<>();
        if (!processor.process(value, banknotes, result)) {
            throw new RuntimeException("Cannot pay this value (insufficient banknotes!)");
        } else {
            subtractCash(result);
        }
    }

    public long getBalance() {
        long balance = 0;
        for (Map.Entry<Banknote, Long> entry: banknotes.entrySet()) {
            long cash = entry.getValue() * entry.getKey().getValue();
            balance += cash;
        }

        return balance;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(String.format("ATM info:\n Avaliable cash:\t %d\n", getBalance()));
        builder.append("Banknote info:\n");
        for (Map.Entry<Banknote, Long> note: banknotes.entrySet()) {
            builder.append(String.format("%s:\t\t\t%d\n", note.getKey().getName(), note.getValue()));
        }
        return builder.toString();
    }

    private Map<Banknote, Long> banknotes;


    private void subtractCash(Map<Banknote, Long> sub) {
        for (Map.Entry<Banknote, Long> b : sub.entrySet()) {
            Banknote tmp = b.getKey();
            if (banknotes.containsKey(tmp)) {
                long r = banknotes.get(tmp) - b.getValue();
                if (r < 0)
                    throw new RuntimeException();

                banknotes.put(tmp, r);
            }
        }
    }
}
