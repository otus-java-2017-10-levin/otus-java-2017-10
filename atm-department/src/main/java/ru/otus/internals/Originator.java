package ru.otus.internals;

public class Originator<T> {

    private T state;

    public void setState(T state) {
        this.state = state;
    }

    public Memento<T> saveToMemento() {
        return new Memento<>(state);
    }

    public void restoreFromMemento(Memento<T> memento) {
        this.state = memento.getState();
    }
}
