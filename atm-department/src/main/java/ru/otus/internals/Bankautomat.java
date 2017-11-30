package ru.otus.internals;

import ru.otus.currency.Banknote;
import ru.otus.currency.Currency;

import java.util.*;

class Bankautomat implements ATM {
    Bankautomat() { banknotes = new HashMap<>(); }

    public void addBanknote(Banknote note, int count) {
        if (note == null) {
            throw new IllegalArgumentException("Wrong banknote");
        }

        if (count <= 0) {
            throw new IllegalArgumentException("Wrong count: " + count);
        }

        int banknotesCount = 0;
        if (banknotes.containsKey(note)) {
            banknotesCount = banknotes.get(note);
        }
        banknotes.put(note, banknotesCount+count);
    }

    public void getCash(long value, Currency currency) throws RuntimeException {

        if (value > getBalance()) {
            throw new RuntimeException("Insufficient funds");
        }
        BanknoteProcessor processor = BanknoteProcessor.createProcessor(currency);

        Map<Banknote, Integer> result = new HashMap<>();
        if (!processor.process(value, banknotes, result)) {
            throw new RuntimeException("Cannot pay this value (insufficient banknotes!)");
        } else {
            subtractCash(result);
        }
    }

    public long getBalance() {
        long balance = 0;
        for (Map.Entry<Banknote, Integer> entry: banknotes.entrySet()) {
            long cash = entry.getValue() * entry.getKey().getValue();
            balance += cash;
        }

        return balance;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(String.format("ATM info:\nAvailable cash:\t %d\n", getBalance()));
        builder.append("Banknote info:\nBanknote\t\tCount\n");

        List<Banknote> list = new ArrayList<>(banknotes.keySet());
        list.sort(Comparator.comparingInt(Banknote::getValue));

        for (Banknote note: list) {
            String currency = note.getSign().equals("") ?
                                    note.getCurrencyName() :
                                    note.getSign();

            builder.append(String.format("%7s%s\t%9d\n", note.getName(), currency, banknotes.get(note)));
        }
        return builder.toString();
    }

    private final Map<Banknote, Integer> banknotes;


    private void subtractCash(Map<Banknote, Integer> sub) {
        for (Map.Entry<Banknote, Integer> b : sub.entrySet()) {
            Banknote tmp = b.getKey();
            if (banknotes.containsKey(tmp)) {
                int r = banknotes.get(tmp) - b.getValue();
                if (r < 0)
                    throw new RuntimeException();

                banknotes.put(tmp, r);
            }
        }
    }
}
