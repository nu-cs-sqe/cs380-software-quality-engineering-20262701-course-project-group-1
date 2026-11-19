# BVA Analysis for `TerminalPrinting`

This file holds the BVA analysis for every public method of the `TerminalPrinting` class. Each public method has its own `## Method N:` section; new methods append a new section as the class grows.

Minimalist **dev-only** output: plain lines on a `PrintStream`. Card names are printed as `CardType` enum strings so the card set can change without rewriting formatters. No polished UX.

Made with assistance from GenAI.

---

## Method 1: `public TerminalPrinting(PrintStream out)`

##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|-------------------|-----------------|--------------|
| TC1 | `new TerminalPrinting(out)` | constructs without exception | yes |

---

## Method 2: `public void printSetup(List<Player> players, Player currentPlayer)`

##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|-------------------|-----------------|--------------|
| TC2 | 2 players `"A"`, `"B"`; current `"A"` | prints `"A alive"`, `"B alive"`, `"Current turn: A"` | yes |
| TC3 | 4 players `"A"`, `"B"`, `"C"`, `"D"`; current `"D"` | prints `"A alive"`, `"B alive"`, `"C alive"`, `"D alive"`, `"Current turn: D"` | yes |

---

## Method 3: `public void printTurn(Player current, int turnsRemaining, int drawPileSize)`

##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|-------------------|-----------------|--------------|
| TC4 | current `"A"`, turns `1`, draw pile size `5` | prints `"Turn: A"`, `"Turns remaining: 1"`, `"Draw pile size: 5"` | yes |
| TC5 | current `"B"`, turns `2`, draw pile size `0` | prints `"Turn: B"`, `"Turns remaining: 2"`, `"Draw pile size: 0"` | yes |

---

## Method 4: `public void printHand(List<Card> hand)`

Numbered list, index starting at `0`. Empty hand prints a single empty-hand line.

##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|-------------------|-----------------|--------------|
| TC6 | hand `[Defuse, Attack]` | prints `"0: Defuse"`, `"1: Attack"` | yes |
| TC7 | empty hand | prints `"Hand empty"` | yes |
| TC8 | hand `[See_The_Future, Shuffle, Defuse]` | prints `"0: See_The_Future"`, `"1: Shuffle"`, `"2: Defuse"` | yes |

---

## Method 5: `public void printDiscardTop(List<Card> discardPile)`

##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|-------------------|-----------------|--------------|
| TC9 | empty discard | prints `"Discard: empty"` | yes |
| TC10 | discard ends with Attack | prints `"Discard top: Attack"` | yes |

---

## Method 6: `public void printSeeTheFuture(List<Card> topCards)`

Top of draw pile first. Does not mutate the list.

##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|-------------------|-----------------|--------------|
| TC11 | `[Exploding_Kitten, Defuse, Shuffle]` | prints a header then `"1: Exploding_Kitten"`, `"2: Defuse"`, `"3: Shuffle"` | yes |
| TC12 | empty list | prints header then `"No cards"` | yes |

---

## Method 7: `public void printEliminated(Player player)`

##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|-------------------|-----------------|--------------|
| TC13 | player `"A"` | prints a line containing `"A"` and that they are eliminated | yes |

---

## Method 8: `public void printWinner(Player winner)`

##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|-------------------|-----------------|--------------|
| TC14 | winner `"B"` | prints a line containing `"B"` and that they won | yes |

---

## Method 9: `public void printMessage(String message)`

##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|-------------------|-----------------|--------------|
| TC15 | `"error"` | prints exactly `"error"` (plus newline) | yes |

---

## Method 10: `public void printGambleResult(boolean turnEnded)`

##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|-------------------|-----------------|--------------|
| TC16 | `printGambleResult(true)` | prints exactly `"Gamble Cat: lucky! Your turn ends without drawing"` | no |
| TC17 | `printGambleResult(false)` | prints exactly `"Gamble Cat: unlucky! Draw two cards to end your turn"` | no |
