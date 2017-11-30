package ru.otus.internals;


import org.junit.jupiter.api.Test;
import ru.otus.currency.CurrencyHelper;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
class BanknoteProcessorTest {

    @Test
    void testStatic() {
        BanknoteProcessor.createProcessor(CurrencyHelper.createCurrency("Ruble"));
    }
}