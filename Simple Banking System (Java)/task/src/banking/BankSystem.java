package banking;

import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;

/**
 * Drives the console banking system: persists accounts to a {@link Database}, tracks the
 * currently logged-in account, prints menus, and implements every menu action (create
 * account, log in/out, check balance, add income, transfer between accounts, close
 * account, exit).
 *
 * <p>Reads user input from the {@link Scanner} supplied at construction time;
 * callers should pass in a single shared {@code Scanner} wrapping {@code System.in}
 * rather than creating new ones, to avoid losing buffered input.
 */
public class BankSystem {

    private final Scanner scanner;
    private final Database database;
    private BankAccount currentAccount;

    /**
     * @param scanner shared scanner used to read all user input (typically wrapping {@code System.in})
     * @param database backing store for created accounts
     */
    public BankSystem(Scanner scanner, Database database) {
        this.scanner = scanner;
        this.database = database;
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
        System.out.println("2. Add income");
        System.out.println("3. Do transfer");
        System.out.println("4. Close account");
        System.out.println("5. Log out");
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
        } while (database.cardNumberExists(bankAccount.getCardNumber()));

        database.insertAccount(bankAccount);
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

        Optional<BankAccount> bankAccount = database.findByCardNumber(cardNumber);
        if (bankAccount.isEmpty() || bankAccount.get().getPin() != pinCode) {
            System.out.println("Wrong card number or PIN!");
            return false;
        }

        System.out.println("You have successfully logged in!");
        currentAccount = bankAccount.get();
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
     * Prompts for an amount and adds it to the currently logged-in account's balance,
     * persisting the change to the database.
     *
     * @throws NullPointerException if no account is currently logged in
     */
    public void addIncome() {
        Objects.requireNonNull(currentAccount);
        System.out.println("Enter income:");
        int income = Integer.parseInt(scanner.nextLine());

        int newBalance = (int) currentAccount.getBalance() + income;
        database.updateBalance(currentAccount.getCardNumber(), newBalance);
        currentAccount.setBalance(newBalance);
        System.out.println("Income was added!");
    }

    /**
     * Prompts for a receiver's card number and an amount, validates the transfer, and —
     * if everything checks out — moves the money from the currently logged-in account to
     * the receiver's account, persisting both balances to the database.
     *
     * <p>Validation order: the receiver must differ from the sender, must be a Luhn-valid
     * card number, must belong to an existing account, and the sender must have enough
     * balance to cover the amount. The first failing check prints its message and aborts
     * the transfer.
     *
     * @throws NullPointerException if no account is currently logged in
     */
    public void doTransfer() {
        Objects.requireNonNull(currentAccount);
        System.out.println("Transfer");
        System.out.println("Enter card number:");
        String receiverCardNumber = scanner.nextLine();

        if (receiverCardNumber.equals(currentAccount.getCardNumber())) {
            System.out.println("You can't transfer money to the same account!");
            return;
        }
        if (!BankAccount.isLuhnValid(receiverCardNumber)) {
            System.out.println("Probably you made a mistake in the card number. Please try again!");
            return;
        }
        Optional<BankAccount> receiver = database.findByCardNumber(receiverCardNumber);
        if (receiver.isEmpty()) {
            System.out.println("Such a card does not exist.");
            return;
        }

        System.out.println("Enter how much money you want to transfer:");
        int amount = Integer.parseInt(scanner.nextLine());
        if (amount > currentAccount.getBalance()) {
            System.out.println("Not enough money!");
            return;
        }

        int senderBalance = (int) currentAccount.getBalance() - amount;
        int receiverBalance = (int) receiver.get().getBalance() + amount;
        database.updateBalance(currentAccount.getCardNumber(), senderBalance);
        database.updateBalance(receiverCardNumber, receiverBalance);
        currentAccount.setBalance(senderBalance);
        System.out.println("Success!");
    }

    /**
     * Deletes the currently logged-in account from the database and logs the user out.
     *
     * @return {@code false}, so callers can assign the result directly to their
     *         "is logged in" flag
     * @throws NullPointerException if no account is currently logged in
     */
    public boolean closeAccount() {
        Objects.requireNonNull(currentAccount);
        database.deleteAccount(currentAccount.getCardNumber());
        currentAccount = null;
        System.out.println("The account has been closed!");
        return false;
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
