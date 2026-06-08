package banking;

import java.util.Scanner;

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
