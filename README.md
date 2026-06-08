# Simple Banking System (Java)

A console-based banking system built as part of the
[Hyperskill "Simple Banking System" project](https://hyperskill.org/projects/93). This repository
currently covers **Stage 1 — Card anatomy** through **Stage 4 — Advanced system**, including
SQLite-backed persistence, income, transfers between accounts, and account closure.

## What it does

Running the program presents a menu-driven console interface. Logged out:

```
1. Create an account
2. Log into account
0. Exit
```

- **Create an account** — generates a unique 16-digit, Luhn-valid card number (`400000` bank
  identification number followed by a 9-digit account identifier and a check digit) and a random
  4-digit PIN (`0000`-`9999`), then persists the new account to the SQLite database with a
  starting balance of `0`.
- **Log into account** — prompts for a card number and PIN; on success, switches to the logged-in
  menu below.
- **Exit** — prints `Bye!` and stops the program, closing any open database connection.

Logged in:

```
1. Balance
2. Add income
3. Do transfer
4. Close account
5. Log out
0. Exit
```

- **Balance** — prints the account's current balance (`Balance: 0` for a fresh account).
- **Add income** — prompts for an amount and adds it to the account's balance.
- **Do transfer** — prompts for a receiver's card number and an amount, then validates the
  transfer (rejecting transfers to the same account, Luhn-invalid numbers, nonexistent cards, and
  amounts exceeding the sender's balance) before moving the money between both accounts.
- **Close account** — deletes the account from the database and returns to the logged-out menu.
- **Log out** — returns to the logged-out menu without deleting the account.

Accounts (including their balances) are stored in a SQLite database file (in a `card` table), so
they survive program restarts — an account created in one run can log in, receive income, send
or receive transfers, and be closed in any later run against the same database file.

## Project layout

```
Simple Banking System (Java)/task/
├── src/banking/
│   ├── Main.java         entry point: parses -fileName, runs the menu loop and action dispatch
│   ├── BankSystem.java   menus, login/logout/balance logic, talks to the Database
│   ├── BankAccount.java  account data + card number (Luhn) / PIN generation
│   └── Database.java     SQLite persistence: schema setup, insert/update/delete, lookup by card number
└── test/
    └── SimpleBankSystemTest.java   Hyperskill StageTest suite for the current stage
```

## Building and running

This is a Gradle project using the Hyperskill plugin, which wires the `task` directory above
(plus a shared `util` module) into the buildable subproject
`:Simple_Banking_System__Java_-task`.

The program requires a `-fileName <name>` argument naming the SQLite database file to use (it is
created automatically on first run if it doesn't exist yet):

```bash
./gradlew :Simple_Banking_System__Java_-task:build                                    # compile and run tests
./gradlew :Simple_Banking_System__Java_-task:test                                     # run the StageTest suite
./gradlew :Simple_Banking_System__Java_-task:run --args="-fileName card.s3db"         # run the program interactively
```

See [`CHANGELOG.md`](CHANGELOG.md) for notable changes, and [`CLAUDE.md`](CLAUDE.md) for more
detailed architecture notes aimed at AI coding assistants.
