package banking;

import java.util.Scanner;

/**
 * Entry point for the console banking system.
 *
 * <p>Runs the menu loop: shows the appropriate menu depending on whether a user is
 * logged in, reads the chosen option, and dispatches it to {@link BankSystem}. The
 * loop keeps running until the user selects "Exit".
 */
public class Main {

    private static boolean isLogged = false;
    private static final Scanner scanner = new Scanner(System.in);
    private static final BankSystem bankSystem = new BankSystem(scanner);

    public static void main(String[] args) {

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
    private static boolean performAction(int action) {
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

}
