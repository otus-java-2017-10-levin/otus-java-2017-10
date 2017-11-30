package ru.otus;

import ru.otus.currency.BanknoteNames;
import ru.otus.currency.Currency;
import ru.otus.currency.CurrencyHelper;
import ru.otus.internals.ATM;
import ru.otus.internals.ATMBuilder;
import ru.otus.io.Command;
import ru.otus.io.Parser;
import ru.otus.io.SimpleParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@SuppressWarnings("IfCanBeSwitch")
class Driver {

    private static ATM atm;
    private static final String RUBLE = "Ruble";
    private Currency currency;
    private Parser parser;

    public static void main(String[] args) {
        Driver driver = new Driver();

        driver.run();
    }

    private void run() {
        currency = CurrencyHelper.createCurrency(RUBLE);
        parser = new SimpleParser();
        parser.addOption("balance", false, "shows ATM info including balance");
        parser.addOption("get", true, "get cash from ATM. Example get 120000");
        parser.addOption("add", true, "add banknote for ATM. For example: add 5000 100 - adds 100 banknotes of 5000 value");

        try {
            atm = new ATMBuilder().addBanknote(currency.get(BanknoteNames.FIVE_THOUSAND), 100)
                    .addBanknote(currency.get(BanknoteNames.TWO_THOUSAND), 100)
                    .addBanknote(currency.get(BanknoteNames.THOUSAND), 100)
                    .addBanknote(currency.get(BanknoteNames.FIVE_HUNDRED), 100)
                    .build();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        System.out.println(parser.getHelp());

        String line;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String question = ">";
            while (!(line = readLine(question, reader)).equals("quit")) {
                Command cmd = new Command();
                try {
                    if (line.equals(""))
                        continue;
                    parser.parse(line, cmd);
                    execCommand(cmd, atm);
                } catch (RuntimeException e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void execCommand(Command cmd, ATM atm) {
        String name = cmd.getName().toLowerCase();
        if (name.equals("balance")) {
            execBalance(atm);

        } else if (name.equals("get")) {
            execGetCash(cmd, atm);
        } else if (name.equals("add")) {
            try {
                execAddCash(cmd, atm);
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
            }
            printATM(atm);
        } else {
            System.out.println(parser.getHelp());
        }
    }

    private String readLine(String question, BufferedReader reader) throws IOException {
        System.out.print(question);
        return reader.readLine();
    }

    private void execAddCash(Command cmd, ATM atm) {
        BanknoteNames banknote = getBanknote(cmd);
        if (banknote == null) {
            throw new RuntimeException("Banknote not found.");
        }

        long value = cmd.getLong();
        atm.addBanknote(currency.get(banknote), (int)value);
    }

    private BanknoteNames getBanknote(Command cmd) {
        long tmp = cmd.getLong();

        return BanknoteNames.get((int)tmp);
    }

    private void execBalance(ATM atm) {
        System.out.println(currency);
        printATM(atm);
    }

    private void execGetCash(Command cmd, ATM atm) {
        long value = cmd.getLong();
        atm.getCash(value, currency);
        printATM(atm);
    }

    private void printATM(ATM atm) {
        System.out.println(atm);
    }

}