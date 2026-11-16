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
| TC14 | `pickStart(2, random)` after `attackNextPlayer()` left 2 turns owed | `getTurnsRemaining()` is `1` | yes |

---

## Method 2: `public int getCurrentIndex()`

##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|-------------------|-----------------|--------------|
| TC10 | `new TurnManager().getCurrentIndex()` | throws `IllegalStateException` with message `"turnManager.notStarted"` | yes |
| TC11 | `getCurrentIndex()` after `pickStart(2, random)`, `Random` always `0` | `0` | yes (implemented in TC1) |
| TC12 | `getCurrentIndex()` after `pickStart(4, random)`, `Random` always `bound - 1` | `3` | yes (implemented in TC4) |
| TC13 | `getCurrentIndex()` after `pickStart(1, random)` was rejected | throws `IllegalStateException` with message `"turnManager.notStarted"` | yes |


---

## Method 3: `public int getTurnsRemaining()`

##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|-------------------|-----------------|--------------|
| TC15 | `new TurnManager().getTurnsRemaining()` | throws `IllegalStateException` with message `"turnManager.notStarted"` | yes |
| TC16 | `getTurnsRemaining()` after `pickStart(2, random)` | `1` | yes |

---

## Method 4: `public void endTurn()`

##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|-------------------|-----------------|--------------|
| TC17 | `new TurnManager().endTurn()` | throws `IllegalStateException` with message `"turnManager.notStarted"` | yes |
| TC18 | 2 players, index `0`, owes 1, `endTurn()` | `getCurrentIndex()` is `1`, `getTurnsRemaining()` is `1` | yes |
| TC19 | 4 players, index `3`, owes 1, `endTurn()` | `getCurrentIndex()` is `0` (wraps), `getTurnsRemaining()` is `1` | yes |
| TC20 | 2 players, index `1`, owes 2, `endTurn()` | `getCurrentIndex()` is `1` (same player), `getTurnsRemaining()` is `1` | yes |

---

## Method 5: `public void attackNextPlayer()`

##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|-------------------|-----------------|--------------|
| TC21 | `new TurnManager().attackNextPlayer()` | throws `IllegalStateException` with message `"turnManager.notStarted"` | yes |
| TC22 | 2 players, index `0`, owes 1, `attackNextPlayer()` | `getCurrentIndex()` is `1`, `getTurnsRemaining()` is `2` | yes |
| TC23 | 4 players, index `3`, owes 1, `attackNextPlayer()` | `getCurrentIndex()` is `0` (wraps), `getTurnsRemaining()` is `2` | yes |
| TC24 | 2 players, index `1`, owes 2, `attackNextPlayer()` | `getCurrentIndex()` is `0`, `getTurnsRemaining()` is `4` (stacked) | yes |
| TC25 | 2 players, index `1`, owes 1 left from an attack, `attackNextPlayer()` | `getCurrentIndex()` is `0`, `getTurnsRemaining()` is `3` (remaining 1 + 2) | yes |
| TC26 | 2 players, attacked player serves both turns, then `attackNextPlayer()` | `getCurrentIndex()` is `1`, `getTurnsRemaining()` is `2` (no transfer once attack is over) | yes |
| TC27 | `attackNextPlayer()`, then `pickStart(2, random)`, then `attackNextPlayer()` | `getCurrentIndex()` is `1`, `getTurnsRemaining()` is `2` (new game clears attack) | yes |

---

## Method 6: `public void addExtraDraw()`

##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|-------------------|-----------------|--------------|
| TC28 | `new TurnManager().addExtraDraw()` | throws `IllegalStateException` with message `"turnManager.notStarted"` | no |
| TC29 | 2 players, index `0`, `addExtraDraw()`, then `endTurn()` | index still `0`; turns remaining `1`; `isMidDraw()` is `true` | no |
| TC30 | 2 players, index `0`, `addExtraDraw()`, then `endTurn()` twice | index `1`; turns remaining `1`; `isMidDraw()` is `false` | no |
| TC31 | 4 players, index `3`, `addExtraDraw()`, then `endTurn()` twice | index `0` (wraps); `isMidDraw()` is `false` | no |
| TC32 | 2 players, index `1` owes 2 from an attack, `addExtraDraw()`, then `endTurn()` twice | index still `1`; turns remaining `1` (this turn used two draws) | no |
| TC33 | 2 players, index `0`, `addExtraDraw()` twice, then `endTurn()` twice | index `1` (extra draws do not stack) | no |
| TC34 | 2 players, index `0`, `addExtraDraw()`, then `attackNextPlayer()`, then `endTurn()` | index `1`; turns remaining `1` (extra draw cleared, not passed on) | no |

---

## Method 7: `public boolean isMidDraw()`

##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|-------------------|-----------------|--------------|
| TC35 | `new TurnManager().isMidDraw()` | throws `IllegalStateException` with message `"turnManager.notStarted"` | no |
| TC36 | `isMidDraw()` after `pickStart(2, random)` | `false` | no |
| TC37 | `isMidDraw()` after `addExtraDraw()` with no draw yet | `false` (penalty set, but drawing hasn't started) | no |
| TC38 | `isMidDraw()` after `addExtraDraw()` and one `endTurn()` | `true` | no (implemented in TC29) |