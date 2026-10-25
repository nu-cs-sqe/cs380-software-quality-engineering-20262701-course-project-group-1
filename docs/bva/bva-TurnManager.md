# BVA Analysis for `TurnManager`

This file holds the BVA analysis for every public method of the `TurnManager` class. Each public method has its own `## Method N:` section; new methods append a new section as the class grows.



Made with assistance from GenAI.

---

## Method 1: `public TurnManager(List<Player> players, Random random)`

##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|-------------------|-----------------|--------------|
| TC1 | `new TurnManager(List.of(A, B), random)`, `Random` always `0` | `getCurrentPlayer()` is `"A"` | no |
| TC2 | `new TurnManager(List.of(A, B), random)`, `Random` always `bound - 1` | `getCurrentPlayer()` is `"B"` | no |
| TC3 | `new TurnManager(List.of(A, B, C, D), random)`, `Random` always `0` | `getCurrentPlayer()` is `"A"` | no |
| TC4 | `new TurnManager(List.of(A, B, C, D), random)`, `Random` always `bound - 1` | `getCurrentPlayer()` is `"D"` | no |
| TC5 | `new TurnManager(List.of(A), random)` | throws `IllegalArgumentException` with message `"turnManager.playerCount.outOfRange"` | no |
| TC6 | `new TurnManager(List.of(A, B, C, D, E), random)` | throws `IllegalArgumentException` with message `"turnManager.playerCount.outOfRange"` | no |
| TC7 | `new TurnManager(List.of(), random)` | throws `IllegalArgumentException` with message `"turnManager.playerCount.outOfRange"` | no |
| TC8 | `new TurnManager(null, random)` | throws `IllegalArgumentException` with message `"turnManager.players.null"` | no |
| TC9 | `new TurnManager(List.of(A, A), random)` | throws `IllegalArgumentException` with message `"turnManager.players.duplicate"` | no |
| TC10 | `new TurnManager(List.of(A, B), null)` | throws `IllegalArgumentException` with message `"turnManager.random.null"` | no |
| TC11 | original list `.clear()` after construction, `Random` always `bound - 1` | `getCurrentPlayer()` is still `"B"` | no |

---

## Method 2: `public Player getCurrentPlayer()`

##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|-------------------|-----------------|--------------|
| TC12 | `getCurrentPlayer()` after construction with 2 players, `Random` always `0` | `"A"` | no (implemented in TC1) |
| TC13 | `getCurrentPlayer()` after construction with 4 players, `Random` always `bound - 1` | `"D"` | no (implemented in TC4) |
| TC14 | `getCurrentPlayer()` called twice with 3 players | same player both times | no |