package ru.otus.internals;

public interface ATMDepartment {

    /**
     * Add an ATM to ATM department.
     * @param atm - atm instance
     * @throws IllegalArgumentException - if {@code atm} - already in ATMDepartment
     * @throws IllegalArgumentException - if {@code atm} is null
     */
    void addATM(ATM atm) throws IllegalArgumentException;

    /**
     * Checks if atm needs to be refilled.
     *  If so, add money to ATM
     *  If no atm found - throw exception
     *  @param atm - atm to refill
     * @throws IllegalArgumentException - throws when {@code atm} not found in {@code ATMDepartment} instance
     */
    void refillATM(ATM atm) throws IllegalArgumentException;

    /**
     *  Add money to all ATMs in ATMDepartment
     */
    void refillAll();

    /**
     * Calculate the summ of available cash through all of added ATMs
     * @return - available cash
     */
    long getAvailableCash();
}
