# BVA Analysis for `TurnManager`

This file holds the BVA analysis for every public method of the `TurnManager` class. Each public method has its own `## Method N:` section; new methods append a new section as the class grows.



Made with assistance from GenAI.

---

## Method 1: `public void pickStart(int playerCount, Random random)`

##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|-------------------|-----------------|--------------|
| TC1 | `pickStart(2, random)`, `Random` always `0` | `getCurrentIndex()` is `0` | yes |
| TC2 | `pickStart(2, random)`, `Random` always `bound - 1` | `getCurrentIndex()` is `1` | yes |
| TC3 | `pickStart(4, random)`, `Random` always `0` | `getCurrentIndex()` is `0` | yes |
| TC4 | `pickStart(4, random)`, `Random` always `bound - 1` | `getCurrentIndex()` is `3` | yes |
| TC5 | `pickStart(1, random)` | throws `IllegalArgumentException` with message `"turnManager.playerCount.outOfRange"` | yes |
| TC6 | `pickStart(5, random)` | throws `IllegalArgumentException` with message `"turnManager.playerCount.outOfRange"` | yes |
| TC7 | `pickStart(0, random)` | throws `IllegalArgumentException` with message `"turnManager.playerCount.outOfRange"` | yes |
| TC8 | `pickStart(2, null)` | throws `IllegalArgumentException` with message `"turnManager.random.null"` | yes |
| TC9 | `pickStart(4, random)` with `Random` always `bound - 1`, then `pickStart(2, random)` with `Random` always `0` | `getCurrentIndex()` is `0` | yes |

---

## Method 2: `public int getCurrentIndex()`

##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|-------------------|-----------------|--------------|
| TC10 | `new TurnManager().getCurrentIndex()` | throws `IllegalStateException` with message `"turnManager.notStarted"` | yes |
| TC11 | `getCurrentIndex()` after `pickStart(2, random)`, `Random` always `0` | `0` | yes (implemented in TC1) |
| TC12 | `getCurrentIndex()` after `pickStart(4, random)`, `Random` always `bound - 1` | `3` | yes (implemented in TC4) |
| TC13 | `getCurrentIndex()` after `pickStart(1, random)` was rejected | throws `IllegalStateException` with message `"turnManager.notStarted"` | yes |