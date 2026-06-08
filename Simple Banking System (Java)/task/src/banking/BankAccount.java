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
     * Reconstructs an account from data already stored elsewhere (e.g. a database row),
     * skipping card number/PIN generation. Package-private: only {@link Database} needs this.
     *
     * @param cardNumber the stored card number
     * @param pin the stored PIN
     * @param balance the stored balance
     * @return an account wrapping the given data
     */
    static BankAccount fromRecord(String cardNumber, int pin, int balance) {
        BankAccount bankAccount = new BankAccount();
        bankAccount.cardNumber = cardNumber;
        bankAccount.pin = pin;
        bankAccount.balance = balance;
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

    /**
     * Updates the in-memory balance, e.g. after an income or transfer has been persisted
     * to the database. Package-private: only {@link BankSystem} needs this.
     *
     * @param balance the new balance
     */
    void setBalance(int balance) {
        this.balance = balance;
    }

    /**
     * Checks whether {@code cardNumber} passes the Luhn ("modulus 10") check: its last digit
     * must equal the check digit computed from the digits preceding it.
     *
     * @param cardNumber the full card number to validate, e.g. a 16-digit number
     * @return {@code true} if the number is Luhn-valid
     */
    public static boolean isLuhnValid(String cardNumber) {
        String prefix = cardNumber.substring(0, cardNumber.length() - 1);
        int checkDigit = cardNumber.charAt(cardNumber.length() - 1) - '0';
        return computeLuhnCheckDigit(prefix) == checkDigit;
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "cardNumber='" + cardNumber + '\'' +
                ", pin='" + pin + '\'' +
                '}';
    }

    /**
     * @return a random 9-digit account identifier, unique enough to keep the
     *         resulting card number distinct in practice
     */
    private static long generateAccountIdentifier() {
        return ThreadLocalRandom.current().nextLong(100_000_000L, 1_000_000_000L);
    }

    /**
     * @return a 16-digit card number that passes the Luhn check: the {@code 400000} bank
     *         identification number (BIN), a 9-digit account identifier, and a check digit
     *         computed by {@link #computeLuhnCheckDigit(String)} so the whole number validates
     */
    private static String generateCardNumber() {
        String prefix = "400000" + generateAccountIdentifier();
        return prefix + computeLuhnCheckDigit(prefix);
    }

    /**
     * Computes the check digit that, appended to {@code prefix}, makes the resulting number
     * pass the Luhn ("modulus 10") check.
     *
     * <p>Walking {@code prefix} from its rightmost digit, every digit at an odd position
     * (1st, 3rd, 5th, ... from the right — i.e. the digits that end up at even positions once
     * the check digit is appended) is doubled, subtracting 9 if the doubled value exceeds 9.
     * The check digit is whatever brings the total sum of all digits to a multiple of 10.
     *
     * @param prefix the digits preceding the check digit, e.g. a 15-digit BIN+account number
     * @return the single check digit (0-9) to append to {@code prefix}
     */
    private static int computeLuhnCheckDigit(String prefix) {
        int sum = 0;
        for (int i = 0; i < prefix.length(); i++) {
            int digit = prefix.charAt(prefix.length() - 1 - i) - '0';
            if (i % 2 == 0) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }
            sum += digit;
        }
        return (10 - (sum % 10)) % 10;
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
