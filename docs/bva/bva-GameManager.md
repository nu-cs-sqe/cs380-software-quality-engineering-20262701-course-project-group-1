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
| TC2 | `startNewGame(List.of("A", "B"))` | 2 alive players named `"A"`, `"B"`; each hand is 6 cards (1 Defuse + 5 GENERIC, no EK); pile has 1 EK and 4 Defuse | no |
| TC3 | `startNewGame(List.of("A", "B", "C", "D"))` | 4 alive players; each hand same as TC2; pile has 3 EK and 2 Defuse | no |
| TC4 | `startNewGame(List.of("A"))` | throws `IllegalArgumentException` with message `"gameManager.playerCount.outOfRange"` | yes |
| TC5 | `startNewGame(List.of("A", "B", "C", "D", "E"))` | throws `IllegalArgumentException` with message `"gameManager.playerCount.outOfRange"` | yes |
| TC6 | `startNewGame(List.of())` | throws `IllegalArgumentException` with message `"gameManager.playerCount.outOfRange"` | yes |
| TC7 | `startNewGame(List.of("", "B"))` | throws `IllegalArgumentException` with message `"gameManager.playerName.blank"` | yes |

---

## Method 3: `public List<Player> getPlayers()`

##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|-------------------|-----------------|--------------|
| TC8 | `new GameManager().getPlayers()` | empty list | yes (implemented in TC1) |
| TC9 | `getPlayers()` after `startNewGame(List.of("A", "B"))` | `["A", "B"]` | yes |
| TC10 | `getPlayers()` after `startNewGame(List.of("A", "B", "C", "D"))` | `["A", "B", "C", "D"]` | yes |
| TC11 | `getPlayers().clear()` after start | later `getPlayers()` size unchanged | no |
| TC12 | `getPlayers()` after `startNewGame(List.of("A"))` | empty list | yes (implemented in TC4) |

---

## Method 4: `public Player getCurrentPlayer()`

##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|-------------------|-----------------|--------------|
| TC13 | `getCurrentPlayer()` after start with 2 players, `Random` always `0` | `"A"` | no |
| TC14 | `getCurrentPlayer()` after start with 2 players, `Random` always `bound - 1` | `"B"` | no |
| TC15 | `getCurrentPlayer()` after start with 4 players, `Random` always `0` | `"A"` | no |
| TC16 | `getCurrentPlayer()` after start with 4 players, `Random` always `bound - 1` | `"D"` | no |

---

## Method 5: `public List<Card> getDrawPile()`

##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|-------------------|-----------------|--------------|
| TC17 | `getDrawPile()` after `startNewGame(List.of("A", "B"))` | 1 EK and 4 Defuse | no (implemented in TC2) |
| TC18 | `getDrawPile()` after `startNewGame(List.of("A", "B", "C", "D"))` | 3 EK and 2 Defuse | no (implemented in TC3) |
| TC19 | `getDrawPile().clear()` after start | later `getDrawPile()` unchanged | no |
