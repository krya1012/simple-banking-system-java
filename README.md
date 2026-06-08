# Simple Banking System (Java)

A console-based banking system built as part of the
[Hyperskill "Simple Banking System" project](https://hyperskill.org/projects/93). This repository
currently contains **Stage 1 — Card anatomy**.

## What it does

Running the program presents a menu-driven console interface:

```
1. Create an account
2. Log into account
0. Exit
```

- **Create an account** — generates a unique 16-digit card number (`400000` bank identification
  number followed by a 10-digit account identifier) and a random 4-digit PIN (`0000`-`9999`), then
  stores the new account in memory with a starting balance of `0`.
- **Log into account** — prompts for a card number and PIN; on success, switches to a logged-in
  menu (`Balance`, `Log out`, `Exit`) where the user can check their balance (`Balance: 0` for a
  fresh account) or log out.
- **Exit** — prints `Bye!` and stops the program.

## Project layout

```
Simple Banking System (Java)/task/
├── src/banking/
│   ├── Main.java         entry point: menu loop and action dispatch
│   ├── BankSystem.java   menus, account storage, login/logout/balance logic
│   └── BankAccount.java  account data + card number / PIN generation
└── test/
    └── SimpleBankSystemTest.java   Hyperskill StageTest suite for this stage
```

## Building and running

This is a Gradle project using the Hyperskill plugin, which wires the `task` directory above
(plus a shared `util` module) into the buildable subproject
`:Simple_Banking_System__Java_-task`.

```bash
./gradlew :Simple_Banking_System__Java_-task:build   # compile and run tests
./gradlew :Simple_Banking_System__Java_-task:test    # run the StageTest suite
./gradlew :Simple_Banking_System__Java_-task:run     # run the program interactively
```

See [`CHANGELOG.md`](CHANGELOG.md) for notable changes, and [`CLAUDE.md`](CLAUDE.md) for more
detailed architecture notes aimed at AI coding assistants.
