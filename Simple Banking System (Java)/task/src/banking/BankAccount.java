package banking;

import java.util.concurrent.ThreadLocalRandom;

public class BankAccount {
    private String cardNumber;
    private int pin;
    private int balance;

    private BankAccount() {}

    public static BankAccount createNewBankAccount() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.cardNumber = generateCardNumber();
        bankAccount.pin = generatePin();
        bankAccount.balance = 0;

        return bankAccount;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public int getPin() {
        return pin;
    }

    public String getFormattedPin() {
        return String.format("%04d", pin);
    }

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

    private static long generateAccountIdentifier() {
        return ThreadLocalRandom.current().nextLong(1000000000L, 9999999999L);
    }

    private static String generateCardNumber() {
        return "400000" + generateAccountIdentifier();
    }

    private static int generatePin() {
        return ThreadLocalRandom.current().nextInt(0, 10000);
    }

    public void printBalance() {
        System.out.println("Balance: " + balance);
    }
}
