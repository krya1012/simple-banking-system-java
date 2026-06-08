# Changelog

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
