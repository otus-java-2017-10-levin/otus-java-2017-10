package ru.otus.internals;

import ru.otus.common.Utils;
import ru.otus.currency.Currency;
import ru.otus.currency.Banknote;
import ru.otus.currency.CurrencyFactory;

import java.util.*;

class Bankautomat implements ATM {

    Bankautomat() {
        banknotes = new HashMap<>();
        currentCurrency = CurrencyFactory.getCurrency(CurrencyFactory.Currencies.ROUBLE);
        ID = ++counter;
    }

    private Bankautomat(Bankautomat bankautomat) {
        this.banknotes = new HashMap<>(bankautomat.banknotes);
        this.currentCurrency = bankautomat.currentCurrency;
        this.ID = bankautomat.ID;
    }

    @Override
    public synchronized void addBanknote(Banknote note, int count) {
        Utils.throwIf(IllegalArgumentException.class, "Wrong banknote", () -> note == null);
        Utils.throwIf(IllegalArgumentException.class, "Wrong count: " + count, () -> count <= 0);

        int banknotesCount = 0;
        if (banknotes.containsKey(note)) {
            banknotesCount = banknotes.get(note);
        }
        banknotes.put(note, banknotesCount + count);
    }

    @Override
    public synchronized void getCash(long value) throws RuntimeException {
        Utils.throwIf(RuntimeException.class, "Insufficient funds", () -> value > getBalance());

        BanknoteProcessor processor = BanknoteProcessor.createProcessor(currentCurrency);

        Map<Banknote, Integer> result = new HashMap<>();
        Utils.throwIf(RuntimeException.class, "Cannot pay this value (insufficient banknotes!)",
                () -> !processor.process(value, banknotes, result));
        subtractCash(result);
    }

    @Override
    public synchronized long getBalance() {
        long balance = 0;
        for (Map.Entry<Banknote, Integer> entry : banknotes.entrySet()) {
            long cash = entry.getValue() * entry.getKey().getValue();
            balance += cash;
        }
        return balance;
    }

    @Override
    public synchronized Integer getId() {
        return ID;
    }

    @Override
    public synchronized Memento<ATM> saveState() {
        return new Memento<>(new Bankautomat(this));
    }

    @Override
    public synchronized void loadState(Memento<ATM> memento) {
        Utils.throwIf(IllegalArgumentException.class, "Cannot rollback - memento = null", () -> memento == null);
        this.banknotes = new HashMap<>(memento.getState().getBanknotes());
    }

    @Override
    public Map<Banknote, Integer> getBanknotes() {
        return Collections.unmodifiableMap(this.banknotes);
    }

    @Override
    public synchronized String toString() {
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

    @Override
    public synchronized int hashCode() {
        return Objects.hash(ID, currentCurrency, banknotes);
    }

    @Override
    public synchronized boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (!(obj instanceof Bankautomat))
            return false;

        Bankautomat bank = (Bankautomat) obj;
        return ID == bank.ID &&
                currentCurrency ==  bank.currentCurrency &&
                Objects.equals(banknotes, bank.banknotes);
    }

    private Map<Banknote, Integer> banknotes;
    private final Currency currentCurrency;
    private static int counter = 0;
    private final int ID;

    private void subtractCash(Map<Banknote, Integer> sub) {
        for (Map.Entry<Banknote, Integer> b : sub.entrySet()) {
            Banknote tmp = b.getKey();
            if (banknotes.containsKey(tmp)) {
                int r = banknotes.get(tmp) - b.getValue();
                Utils.throwIf(RuntimeException.class, "r < 0", () -> r <0);

                banknotes.put(tmp, r);
            }
        }
    }
}