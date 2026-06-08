package banking;

import java.util.concurrent.ThreadLocalRandom;

/**
 * A single bank account: a 16-digit card number, a 4-digit PIN, and a balance.
 *
 * <p>Instances are only created via {@link #createNewBankAccount()}, which generates
 * a fresh card number and PIN and starts the balance at zero.
 */
public class BankAccount {
    private String cardNumber;
    private int pin;
    private int balance;

    private BankAccount() {}

    /**
     * Creates a new account with a freshly generated card number, a random 4-digit PIN
     * (in the range {@code 0000}-{@code 9999}), and a starting balance of zero.
     *
     * @return the newly created account
     */
    public static BankAccount createNewBankAccount() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.cardNumber = generateCardNumber();
        bankAccount.pin = generatePin();
        bankAccount.balance = 0;

        return bankAccount;
    }

    /**
     * @return the 16-digit card number, e.g. {@code 4000001234567890}
     */
    public String getCardNumber() {
        return cardNumber;
    }

    /**
     * @return the PIN as an integer; use {@link #getFormattedPin()} for display, since
     *         this value drops any leading zeros
     */
    public int getPin() {
        return pin;
    }

    /**
     * @return the PIN zero-padded to 4 digits, e.g. {@code "0042"}, suitable for display
     */
    public String getFormattedPin() {
        return String.format("%04d", pin);
    }

    /**
     * @return the current balance
     */
    public double getBalance() {
        return balance;
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "cardNumber='" + cardNumber + '\'' +
                ", pin='" + pin + '\'' +
                '}';
    }

    /**
     * @return a random 10-digit account identifier, unique enough to keep the
     *         resulting card number distinct in practice
     */
    private static long generateAccountIdentifier() {
        return ThreadLocalRandom.current().nextLong(1000000000L, 9999999999L);
    }

    /**
     * @return a 16-digit card number: the {@code 400000} bank identification number (BIN)
     *         followed by a 10-digit account identifier
     */
    private static String generateCardNumber() {
        return "400000" + generateAccountIdentifier();
    }

    /**
     * @return a random 4-digit PIN in the range {@code 0000}-{@code 9999}
     */
    private static int generatePin() {
        return ThreadLocalRandom.current().nextInt(0, 10000);
    }

    /**
     * Prints the current balance to standard output, e.g. {@code "Balance: 0"}.
     */
    public void printBalance() {
        System.out.println("Balance: " + balance);
    }
}
