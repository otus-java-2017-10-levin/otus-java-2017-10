package ru.otus;

class Driver {

    public static void main(String[] args) {
        new Driver().run();
    }

    private void run() {
        int wait = 10_000;
        ATMFacade facade = new ATMFacade();

        facade.createAtm(1);
        facade.start(wait, 5);
    }

}