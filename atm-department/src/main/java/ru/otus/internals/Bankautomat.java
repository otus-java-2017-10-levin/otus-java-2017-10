package ru.otus.internals;

import org.jetbrains.annotations.NotNull;
import ru.otus.common.CommonHelper;
import ru.otus.currency.Currency;
import ru.otus.currency.Banknote;
import ru.otus.currency.CurrencyFactory;

import java.util.*;

class Bankautomat implements ATM {
    Bankautomat() {
        banknotes = new HashMap<>();
        currentCurrency = CurrencyFactory.getCurrency("Rouble");
    }

    @Override
    public void addBanknote(Banknote note, int count) {
        CommonHelper.verify(IllegalArgumentException.class, "Wrong banknote", () -> note == null);
        CommonHelper.verify(IllegalArgumentException.class, "Wrong count: " + count, () -> count <= 0);

        int banknotesCount = 0;
        if (banknotes.containsKey(note)) {
            banknotesCount = banknotes.get(note);
        }
        banknotes.put(note, banknotesCount + count);
    }

    @Override
    public void getCash(long value) throws RuntimeException {
        CommonHelper.verify(RuntimeException.class, "Insufficient funds", () -> value > getBalance());

        BanknoteProcessor processor = BanknoteProcessor.createProcessor(currentCurrency);

        Map<Banknote, Integer> result = new HashMap<>();
        CommonHelper.verify(RuntimeException.class, "Cannot pay this value (insufficient banknotes!)",
                () -> !processor.process(value, banknotes, result));
        subtractCash(result);
    }

    @Override
    public long getBalance() {
        long balance = 0;
        for (Map.Entry<Banknote, Integer> entry : banknotes.entrySet()) {
            long cash = entry.getValue() * entry.getKey().getValue();
            balance += cash;
        }
        return balance;
    }

    @Override
    public void changeCurrency(@NotNull Currency currency) {
        currency = currentCurrency;
    }

    @Override
    @NotNull
    public Currency getCurrency() {
        return currentCurrency;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(String.format("ATM info:\nAvailable cash:\t %d\n", getBalance()));
        builder.append("Banknote info:\nBanknote\t\tCount\n");

        List<Banknote> list = new ArrayList<>(banknotes.keySet());
        list.sort(Comparator.comparingInt(Banknote::getValue));

        for (Banknote note : list) {
            String currency = note.getSign().equals("") ?
                    note.getCurrencyName() :
                    note.getSign();

            builder.append(String.format("%7s%s\t%9d\n", note.getName(), currency, banknotes.get(note)));
        }
        return builder.toString();
    }

    private final Map<Banknote, Integer> banknotes;
    private Currency currentCurrency;


    private void subtractCash(Map<Banknote, Integer> sub) {
        for (Map.Entry<Banknote, Integer> b : sub.entrySet()) {
            Banknote tmp = b.getKey();
            if (banknotes.containsKey(tmp)) {
                int r = banknotes.get(tmp) - b.getValue();
                CommonHelper.verify(RuntimeException.class, null, () -> r <0);

                banknotes.put(tmp, r);
            }
        }
    }
}
