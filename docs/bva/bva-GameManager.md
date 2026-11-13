# BVA Analysis for `GameManager`

This file holds the BVA analysis for every public method of the `GameManager` class. Each public method has its own `## Method N:` section; new methods append a new section as the class grows.



Made with assistance from GenAI. 

---

## Method 1: `public GameManager()`

##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|-------------------|-----------------|--------------|
| TC1 | `new GameManager()` | `getPlayers()` is empty | yes |

---

## Method 2: `public void startNewGame(List<String> playerNames)`

##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|-------------------|-----------------|--------------|
| TC2 | `startNewGame(List.of("A", "B"))` | 2 alive players named `"A"`, `"B"`; each hand is 6 cards (1 Defuse + 5 See_The_Future, no EK); pile has 1 EK and 4 Defuse | yes |
| TC3 | `startNewGame(List.of("A", "B", "C", "D"))` | 4 alive players; each hand same as TC2; pile has 3 EK and 2 Defuse | yes |
| TC4 | `startNewGame(List.of("A"))` | throws `IllegalArgumentException` with message `"gameManager.playerCount.outOfRange"` | yes |
| TC5 | `startNewGame(List.of("A", "B", "C", "D", "E"))` | throws `IllegalArgumentException` with message `"gameManager.playerCount.outOfRange"` | yes |
| TC6 | `startNewGame(List.of())` | throws `IllegalArgumentException` with message `"gameManager.playerCount.outOfRange"` | yes |
| TC7 | `startNewGame(List.of("", "B"))` | throws `IllegalArgumentException` with message `"gameManager.playerName.blank"` | yes |
| TC34 | `startNewGame(List.of("A", "B"))` again after `"A"` played Attack | `getDiscardPile()` is empty; `getTurnsRemaining()` is `1` | no |

---

## Method 3: `public List<Player> getPlayers()`

##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|-------------------|-----------------|--------------|
| TC8 | `new GameManager().getPlayers()` | empty list | yes (implemented in TC1) |
| TC9 | `getPlayers()` after `startNewGame(List.of("A", "B"))` | `["A", "B"]` | yes |
| TC10 | `getPlayers()` after `startNewGame(List.of("A", "B", "C", "D"))` | `["A", "B", "C", "D"]` | yes |
| TC11 | `getPlayers().clear()` after start | later `getPlayers()` size unchanged | yes |
| TC12 | `getPlayers()` after `startNewGame(List.of("A"))` | empty list | yes (implemented in TC4) |

---

## Method 4: `public Player getCurrentPlayer()`

##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|-------------------|-----------------|--------------|
| TC13 | `getCurrentPlayer()` after start with 2 players, `Random` always `0` | `"A"` | yes          |
| TC14 | `getCurrentPlayer()` after start with 2 players, `Random` always `bound - 1` | `"B"` | yes          |
| TC15 | `getCurrentPlayer()` after start with 4 players, `Random` always `0` | `"A"` | yes          |
| TC16 | `getCurrentPlayer()` after start with 4 players, `Random` always `bound - 1` | `"D"` | yes          |

---

## Method 5: `public List<Card> getDrawPile()`

##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|-------------------|-----------------|--------------|
| TC17 | `getDrawPile()` after `startNewGame(List.of("A", "B"))` | 1 EK and 4 Defuse | yes (implemented in TC2) |
| TC18 | `getDrawPile()` after `startNewGame(List.of("A", "B", "C", "D"))` | 3 EK and 2 Defuse | yes (implemented in TC3) |
| TC19 | `getDrawPile().clear()` after start | later `getDrawPile()` unchanged | yes |

---

## Method 6: `public void playAttack()`

##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|-------------------|-----------------|--------------|
| TC20 | `playAttack()` before `startNewGame` | throws `IllegalStateException` with message `"gameManager.notStarted"` | yes |
| TC21 | 3 players, current player `"A"` has no Attack, `playAttack()` | throws `IllegalStateException` with message `"gameManager.play.notInHand"`; hand and current player unchanged | yes |
| TC22 | 2 players, `"A"` holds 1 Attack, `playAttack()` | Attack removed from `"A"`'s hand and on top of discard pile; current player is `"B"`; turns remaining is `2` | yes |
| TC23 | 4 players, `Random` always `0` (`"A"` starts), `"A"` holds 1 Attack, `playAttack()` | current player is `"B"` (next seat, not `"C"` or `"D"`); turns remaining is `2` | yes |
| TC24 | 4 players, `Random` always `bound - 1` (`"D"` starts), `"D"` holds 1 Attack, `playAttack()` | current player is `"A"` (wraps from last seat); turns remaining is `2` | yes |
| TC25 | 3 players, `"A"` holds 2 Attacks, `playAttack()` | exactly one Attack removed; one still in `"A"`'s hand | yes |
| TC26 | 3 players, `"A"` attacks `"B"`, then `"B"` plays Attack | current player is `"C"` (not back to `"A"`); turns remaining is `4` | yes |

---

## Method 7: `public List<Card> getDiscardPile()`

##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|-------------------|-----------------|--------------|
| TC27 | `new GameManager().getDiscardPile()` | empty list | yes |
| TC28 | `getDiscardPile()` after `startNewGame(List.of("A", "B", "C"))` | empty list | yes |
| TC29 | `getDiscardPile()` after `"A"` plays Attack | last element is that Attack card | yes (implemented in TC22) |
| TC30 | `getDiscardPile().clear()` after `"A"` plays Attack | later `getDiscardPile()` still has 1 card | yes |

---

## Method 8: `public int getTurnsRemaining()`

##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|-------------------|-----------------|--------------|
| TC31 | `new GameManager().getTurnsRemaining()` | throws `IllegalStateException` with message `"gameManager.notStarted"` | yes |
| TC32 | `getTurnsRemaining()` after `startNewGame(List.of("A", "B", "C", "D"))` | `1` | yes |
| TC33 | `getTurnsRemaining()` after `"A"` plays Attack | `2` | yes (implemented in TC22) |
