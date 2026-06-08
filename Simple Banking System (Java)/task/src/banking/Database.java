package banking;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

/**
 * SQLite-backed storage for bank accounts, persisted in a {@code card} table
 * (columns: {@code id}, {@code number}, {@code pin}, {@code balance}).
 *
 * <p>Each operation opens its own short-lived {@link Connection} via try-with-resources,
 * so no connection is ever left open between calls or when the program exits.
 */
public class Database {

    private final String connectionUrl;

    /**
     * Connects to (creating if necessary) the SQLite database file at {@code fileName} and
     * ensures its {@code card} table exists.
     *
     * @param fileName path to the database file, e.g. {@code "card.s3db"}
     */
    public Database(String fileName) {
        connectionUrl = "jdbc:sqlite:" + fileName;
        createCardTable();
    }

    private void createCardTable() {
        String sql = "CREATE TABLE IF NOT EXISTS card (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "number TEXT, " +
                "pin TEXT, " +
                "balance INTEGER DEFAULT 0)";

        try (Connection connection = DriverManager.getConnection(connectionUrl);
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize the database", e);
        }
    }

    /**
     * Stores a newly created account as a row in the {@code card} table.
     *
     * @param account the account to persist
     */
    public void insertAccount(BankAccount account) {
        String sql = "INSERT INTO card (number, pin, balance) VALUES (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(connectionUrl);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, account.getCardNumber());
            statement.setString(2, account.getFormattedPin());
            statement.setInt(3, (int) account.getBalance());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save the new account", e);
        }
    }

    /**
     * @param cardNumber the card number to look up
     * @return {@code true} if an account with this card number is already stored
     */
    public boolean cardNumberExists(String cardNumber) {
        return findByCardNumber(cardNumber).isPresent();
    }

    /**
     * Looks up a stored account by its card number.
     *
     * @param cardNumber the card number to look up
     * @return the matching account, or {@link Optional#empty()} if none is stored
     */
    public Optional<BankAccount> findByCardNumber(String cardNumber) {
        String sql = "SELECT number, pin, balance FROM card WHERE number = ?";

        try (Connection connection = DriverManager.getConnection(connectionUrl);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, cardNumber);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return Optional.empty();
                }
                return Optional.of(BankAccount.fromRecord(
                        resultSet.getString("number"),
                        Integer.parseInt(resultSet.getString("pin")),
                        resultSet.getInt("balance")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to look up the account", e);
        }
    }
}
