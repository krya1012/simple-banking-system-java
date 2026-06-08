# Simple Banking System (Java)

A console-based banking system built as part of the
[Hyperskill "Simple Banking System" project](https://hyperskill.org/projects/93). This repository
currently covers **Stage 1 — Card anatomy** through **Stage 3 — I'm so lite**, including SQLite-backed
persistence.

## What it does

Running the program presents a menu-driven console interface:

```
1. Create an account
2. Log into account
0. Exit
```

- **Create an account** — generates a unique 16-digit, Luhn-valid card number (`400000` bank
  identification number followed by a 9-digit account identifier and a check digit) and a random
  4-digit PIN (`0000`-`9999`), then persists the new account to the SQLite database with a
  starting balance of `0`.
- **Log into account** — prompts for a card number and PIN; on success, switches to a logged-in
  menu (`Balance`, `Log out`, `Exit`) where the user can check their balance (`Balance: 0` for a
  fresh account) or log out.
- **Exit** — prints `Bye!` and stops the program, closing any open database connection.

Accounts are stored in a SQLite database file (in a `card` table), so they survive program
restarts — an account created in one run can log in successfully in a later run against the same
database file.

## Project layout

```
Simple Banking System (Java)/task/
├── src/banking/
│   ├── Main.java         entry point: parses -fileName, runs the menu loop and action dispatch
│   ├── BankSystem.java   menus, login/logout/balance logic, talks to the Database
│   ├── BankAccount.java  account data + card number (Luhn) / PIN generation
│   └── Database.java     SQLite persistence: schema setup, insert, lookup by card number
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
