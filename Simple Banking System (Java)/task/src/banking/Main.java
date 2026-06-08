package banking;

import java.util.Scanner;

/**
 * Entry point for the console banking system.
 *
 * <p>Runs the menu loop: shows the appropriate menu depending on whether a user is
 * logged in, reads the chosen option, and dispatches it to {@link BankSystem}. The
 * loop keeps running until the user selects "Exit".
 *
 * <p>Expects a {@code -fileName <name>} command-line argument naming the SQLite
 * database file used to persist accounts, e.g. {@code -fileName card.s3db}.
 */
public class Main {

    private boolean isLogged = false;
    private final Scanner scanner = new Scanner(System.in);
    private final BankSystem bankSystem;

    public Main(String[] args) {
        bankSystem = new BankSystem(scanner, new Database(parseFileName(args)));
    }

    public static void main(String[] args) {
        new Main(args).run();
    }

    private void run() {
        boolean running = true;
        while (running) {
            if (isLogged) {
                bankSystem.showLoginMenu();
            } else {
                bankSystem.showStartMenu();
            }
            int action = Integer.parseInt(scanner.nextLine());
            running = performAction(action);
        }
    }

    /**
     * Dispatches a single menu choice to the matching {@link BankSystem} action.
     * The available actions depend on whether a user is currently logged in.
     *
     * @param action the numeric menu option chosen by the user
     * @return {@code true} if the program should keep running, {@code false} if the
     *         user chose to exit
     */
    private boolean performAction(int action) {
        switch (action) {
            case 1:
                if (!isLogged) {
                    bankSystem.createAccount();
                } else {
                    bankSystem.printBalance();
                }
                break;
            case 2:
                if (!isLogged) {
                    isLogged = bankSystem.logIntoAccount();
                } else {
                    isLogged = bankSystem.logOut();
                }
                break;
            case 0:
                bankSystem.exit();
                return false;
            default:
                System.out.println("Wrong option");
        }
        return true;
    }

    /**
     * Extracts the database file name from the {@code -fileName <name>} command-line argument.
     *
     * @param args the program's command-line arguments
     * @return the file name following {@code -fileName}
     * @throws IllegalArgumentException if {@code -fileName <name>} is not present
     */
    private static String parseFileName(String[] args) {
        for (int i = 0; i < args.length - 1; i++) {
            if (args[i].equals("-fileName")) {
                return args[i + 1];
            }
        }
        throw new IllegalArgumentException("Missing required \"-fileName <name>\" argument");
    }

}
