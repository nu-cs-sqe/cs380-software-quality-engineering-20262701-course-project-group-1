# BVA Analysis for `TerminalPrinting`

This file holds the BVA analysis for every public method of the `TerminalPrinting` class. Each public method has its own `## Method N:` section; new methods append a new section as the class grows.

Setup output only: seated names, alive status, and whose turn it is. Card names and hand contents are out of scope so the card set can still change.

Made with assistance from GenAI.

---

## Method 1: `public TerminalPrinting(PrintStream out)`

##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|-------------------|-----------------|--------------|
| TC1 | `new TerminalPrinting(out)` | constructs without exception | no |

---

## Method 2: `public void printSetup(List<Player> players, Player currentPlayer)`

##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|-------------------|-----------------|--------------|
| TC2 | 2 players `"A"`, `"B"`; current `"A"` | prints `"A alive"`, `"B alive"`, `"Current turn: A"` | no |
| TC3 | 4 players `"A"`, `"B"`, `"C"`, `"D"`; current `"D"` | prints `"A alive"`, `"B alive"`, `"C alive"`, `"D alive"`, `"Current turn: D"` | no |
