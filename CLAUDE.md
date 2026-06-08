# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project overview

This is a JetBrains Academy / Hyperskill "edu" course project ("Simple Banking System (Java)",
project #93). The repo is a multi-stage learning project where each stage adds functionality to a
console-based banking system. The directories `Card anatomy`, `Luhn algorithm`, `I'm so lite`, and
`Advanced system` each hold a stage's `task-info.yaml` and `task.html` (the task description shown
to the learner) — they are reference material, **not** separate buildable modules (no `src`/`test`
inside them).

The actual buildable code lives in:

```
Simple Banking System (Java)/task/
├── src/banking/        Main.java, BankAccount.java, BankSystem.java
└── test/               SimpleBankSystemTest.java (Hyperskill StageTest, generated — don't hand-edit)
```

Read the relevant stage's `task.html` (e.g. `Simple Banking System (Java)/Advanced system/task.html`)
before implementing a feature — it contains the exact expected menu text, prompts, error messages,
and example I/O that the bundled `SimpleBankSystemTest` checks against verbatim.

## Build & test commands

The Gradle root project wires each stage's `task` directory plus `util` into subprojects (see
`settings.gradle`, which auto-discovers any directory containing `src` or `test`). The current
buildable subproject is `:Simple_Banking_System__Java_-task` (note the sanitized name with
underscores/parentheses stripped).

```bash
./gradlew projects                                          # list subprojects/module names
./gradlew :Simple_Banking_System__Java_-task:build           # compile + run tests
./gradlew :Simple_Banking_System__Java_-task:test            # run the StageTest suite
./gradlew :Simple_Banking_System__Java_-task:run             # run Main interactively (needs stdin)
./gradlew :Simple_Banking_System__Java_-task:test --tests "SimpleBankSystemTest.test1_checkCardCredentials"  # single test
```

Tests use the Hyperskill `hs-test` framework (`StageTest` + `TestedProgram`): each `@DynamicTest`
spins up the program, feeds it lines of input via `program.execute(...)`, and asserts on the
captured stdout against regex patterns / expected strings. When changing console output text,
prompts, or menu wording, check `test/SimpleBankSystemTest.java` and the stage's `task.html`
examples — exact string matches matter.

Dependencies (`org.xerial:sqlite-jdbc`, `com.github.hyperskill:hs-test`) are resolved from Maven
Central plus the Hyperskill/JitBrains JitPack repos declared in `build.gradle`/`settings.gradle`.

## Architecture

Simple three-class console app driven by a blocking input loop:

- **`Main`** — owns the `Scanner`/REPL loop and a single `BankSystem` instance, tracks `isLogged`
  state, and dispatches numeric menu choices to `BankSystem` methods (`performAction`).
- **`BankSystem`** — holds the in-memory account store (`Map<cardNumber, BankAccount>`), the
  currently logged-in account, prints menus, and implements all user-facing operations
  (create account, log in/out, print balance, exit).
- **`BankAccount`** — immutable-ish data holder for a single account (card number, PIN, balance);
  `createNewBankAccount()` is the only way to construct one and generates a Luhn-style card number
  (`"400000" + 10 random digits`) and a 4-digit PIN via `ThreadLocalRandom`.

Later stages (per `task-info.yaml`/`task.html` for "I'm so lite" and "Advanced system") introduce a
`Database` class backed by SQLite (`sqlite-jdbc` dependency is already present in `build.gradle`)
and extend `BankSystem` with income, transfers (validated with the Luhn algorithm), and account
deletion — none of that exists yet in the current `task/src`, so check the relevant stage's
`task.html` for the exact expected behavior/output before adding it.