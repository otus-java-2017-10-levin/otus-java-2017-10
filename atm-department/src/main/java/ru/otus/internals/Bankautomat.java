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
        currentCurrency = CurrencyFactory.getCurrency(CurrencyFactory.Currencies.ROUBLE);
    }

    private Bankautomat(Map<Banknote, Integer> banknotes, Currency currentCurrency) {
        this.banknotes = new HashMap<>(banknotes);
        this.currentCurrency = currentCurrency;
    }

    @Override
    public void addBanknote(Banknote note, int count) {
        CommonHelper.throwIf(IllegalArgumentException.class, "Wrong banknote", () -> note == null);
        CommonHelper.throwIf(IllegalArgumentException.class, "Wrong count: " + count, () -> count <= 0);

        int banknotesCount = 0;
        if (banknotes.containsKey(note)) {
            banknotesCount = banknotes.get(note);
        }
        banknotes.put(note, banknotesCount + count);
    }

    @Override
    public void getCash(long value) throws RuntimeException {
        CommonHelper.throwIf(RuntimeException.class, "Insufficient funds", () -> value > getBalance());

        BanknoteProcessor processor = BanknoteProcessor.createProcessor(currentCurrency);

        Map<Banknote, Integer> result = new HashMap<>();
        CommonHelper.throwIf(RuntimeException.class, "Cannot pay this value (insufficient banknotes!)",
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
    public Integer getId() {
        return ID;
    }

    @Override
    @NotNull
    public Currency getCurrency() {
        return currentCurrency;
    }

    @Override
    public void saveState() {
        if (savedState.size() == 0) {
            saveState(new Bankautomat(banknotes, currentCurrency));
        } else {
            int pos = savedState.size() - 1;
            Bankautomat tmp = savedState.get(pos).getState();
            if (!this.equals(tmp)) {
                saveState(new Bankautomat(banknotes, currentCurrency));
            }
        }
    }

    @Override
    public void loadState(STATES currentState) {
        CommonHelper.throwIf(IllegalArgumentException.class, "savedState is empty", () -> savedState.size() == 0);

        if (currentState == STATES.INITIAL) {
            loadState(0);
        } else {
            throw new IllegalStateException("not supported operation");
        }
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

    @Override
    public int hashCode() {
        return Objects.hash(ID, currentCurrency, banknotes);
    }

    @Override
    public boolean equals(Object obj) {
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
    private Currency currentCurrency;
    private Originator<Bankautomat> originator = new Originator<>();
    private final List<Memento<Bankautomat>> savedState = new ArrayList<>();
    private static int counter = 0;
    private final int ID = counter++;

    private void subtractCash(Map<Banknote, Integer> sub) {
        for (Map.Entry<Banknote, Integer> b : sub.entrySet()) {
            Banknote tmp = b.getKey();
            if (banknotes.containsKey(tmp)) {
                int r = banknotes.get(tmp) - b.getValue();
                CommonHelper.throwIf(RuntimeException.class, null, () -> r <0);

                banknotes.put(tmp, r);
            }
        }
    }

    private void loadState(int pos) {
        Memento<Bankautomat> memento = savedState.get(pos);

        this.banknotes = memento.getState().banknotes;
        this.currentCurrency = memento.getState().currentCurrency;

        clearState();
    }

    private void clearState() {
        savedState.clear();
        saveState();
    }

    private void saveState(Bankautomat bankautomat) {
        originator.setState(bankautomat);
        savedState.add(originator.saveToMemento());

    }
}