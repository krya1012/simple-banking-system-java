package banking;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

/**
 * Drives the console banking system: stores all created accounts, tracks the
 * currently logged-in account, prints menus, and implements every menu action
 * (create account, log in/out, check balance, exit).
 *
 * <p>Reads user input from the {@link Scanner} supplied at construction time;
 * callers should pass in a single shared {@code Scanner} wrapping {@code System.in}
 * rather than creating new ones, to avoid losing buffered input.
 */
public class BankSystem {

    private final Map<String, BankAccount> accounts = new HashMap<>();
    private final Scanner scanner;
    private BankAccount currentAccount;

    /**
     * @param scanner shared scanner used to read all user input (typically wrapping {@code System.in})
     */
    public BankSystem(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Prints the menu shown to a user who is not currently logged in.
     */
    public void showStartMenu() {
        System.out.println("1. Create an account");
        System.out.println("2. Log into account");
        System.out.println("0. Exit");
    }

    /**
     * Prints the menu shown to a user who is currently logged in.
     */
    public void showLoginMenu() {
        System.out.println("1. Balance");
        System.out.println("2. Log out");
        System.out.println("0. Exit");
    }

    /**
     * Generates a new account with a unique card number, stores it, and prints its
     * card number and PIN to the console. Regenerates the card number on the rare
     * chance of a collision with an existing account.
     */
    public void createAccount() {
        BankAccount bankAccount;
        do {
            bankAccount = BankAccount.createNewBankAccount();
        } while (accounts.containsKey(bankAccount.getCardNumber()));

        accounts.put(bankAccount.getCardNumber(), bankAccount);
        System.out.println("Your card has been created");
        System.out.println("You card number:");
        System.out.println(bankAccount.getCardNumber());
        System.out.println("Your card PIN:");
        System.out.println(bankAccount.getFormattedPin());
    }

    /**
     * Prompts for a card number and PIN and, if they match a stored account, logs the
     * user into it.
     *
     * @return {@code true} if the credentials matched and the user is now logged in,
     *         {@code false} otherwise
     */
    public boolean logIntoAccount() {
        System.out.println("Enter your card number:");
        String cardNumber = scanner.nextLine();
        System.out.println("Enter your PIN:");
        int pinCode = Integer.parseInt(scanner.nextLine());

        BankAccount bankAccount = accounts.get(cardNumber);
        if (bankAccount == null || bankAccount.getPin() != pinCode) {
            System.out.println("Wrong card number or PIN!");
            return false;
        }

        System.out.println("You have successfully logged in!");
        currentAccount = bankAccount;
        return true;
    }

    /**
     * Prints the balance of the currently logged-in account.
     *
     * @throws NullPointerException if no account is currently logged in
     */
    public void printBalance() {
        Objects.requireNonNull(currentAccount);
        currentAccount.printBalance();
    }

    /**
     * Logs the current user out.
     *
     * @return {@code false}, so callers can assign the result directly to their
     *         "is logged in" flag
     * @throws NullPointerException if no account is currently logged in
     */
    public boolean logOut() {
        Objects.requireNonNull(currentAccount);
        currentAccount = null;
        System.out.println("You have successfully logged out!");
        return false;
    }

    /**
     * Prints the farewell message shown when the user chooses to exit.
     *
     * <p>Does not terminate the JVM itself — the caller's main loop is expected to stop
     * iterating, since calling {@code System.exit} here would also kill the test harness.
     */
    public void exit() {
        System.out.println("Bye!");
    }
}
