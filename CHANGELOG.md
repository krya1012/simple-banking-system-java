# Changelog

## Stage 3 — I'm so lite

### Added

- New `Database` class that persists accounts in a SQLite `card` table
  (`id INTEGER`, `number TEXT`, `pin TEXT`, `balance INTEGER DEFAULT 0`), creating the
  database file and table on startup if they don't exist yet. Each operation opens and
  closes its own connection via try-with-resources, so no connection is left open when
  the program exits.
- `BankAccount.fromRecord(...)`, a package-private factory for reconstructing an account
  from a stored database row.

### Changed

- The database file name is now read from a required `-fileName <name>` command-line
  argument (e.g. `-fileName card.s3db`); `Main` is now instance-based so this value can
  flow from `args` into the `Database`/`BankSystem` constructors.
- `BankSystem` no longer stores accounts in an in-memory map — `createAccount` and
  `logIntoAccount` now read from and write to the `Database`, so accounts survive
  restarts.
- The bundled `SimpleBankSystemTest` was replaced by Hyperskill's official stage-3 suite
  (it now verifies the database file/table/schema, persisted rows, and that the program
  fully terminates and closes its database connection on exit).

## Stage 2 — Luhn algorithm

### Changed

- Card numbers are now generated to pass the Luhn ("modulus 10") checksum, as required by this
  stage, instead of using an arbitrary last digit. `BankAccount` now builds each 16-digit card
  number from the `400000` BIN, a random 9-digit account identifier, and a check digit computed
  by the new `computeLuhnCheckDigit` helper so the full number validates under the Luhn algorithm.

## Stage 1 — Card anatomy

Initial implementation of the console banking system covering account creation, login/logout,
and balance checking.

### Fixed

- Removed the `System.exit(0)` call from `BankSystem.exit()` — it caused the Hyperskill test
  harness to fail outright (`System.exit()` terminates the test JVM along with the program under
  test). The main loop in `Main` now terminates naturally by returning `false` from
  `performAction` when the user chooses "Exit".
- Corrected the login prompt from `"Enter your card number;"` to `"Enter your card number:"` to
  match the stage specification.
- Widened PIN generation from `1000`-`9998` to the full `0000`-`9999` range specified by the
  stage, and added `BankAccount.getFormattedPin()` to zero-pad PINs below `1000` to 4 digits when
  displayed.
- Added a uniqueness check when generating new card numbers: `BankSystem.createAccount()` now
  regenerates the account on the rare chance of a collision with an existing card number, so two
  accounts can never share a number.
- Replaced the per-call `new Scanner(System.in)` inside `BankSystem.logIntoAccount()` with a
  single `Scanner` shared across `Main` and `BankSystem` (passed in via the constructor), avoiding
  the risk of buffered input being lost between independent `Scanner` instances on `System.in`.

### Added

- Javadoc for the public API of `Main`, `BankSystem`, and `BankAccount`.
