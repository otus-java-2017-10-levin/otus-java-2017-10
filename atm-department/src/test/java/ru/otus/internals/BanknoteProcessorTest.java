package ru.otus.internals;


import org.junit.jupiter.api.Test;
import ru.otus.currency.CurrencyHelper;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class BanknoteProcessorTest {

    @Test
    public void testStatic() {
        BanknoteProcessor processor = BanknoteProcessor.createProcessor(CurrencyHelper.createCurrency("Ruble"));

    }
}