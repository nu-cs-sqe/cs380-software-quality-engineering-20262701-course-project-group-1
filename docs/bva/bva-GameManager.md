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

Starting cards: **10** See_The_Future, **5** Attack, **5** Shuffle, **6** Defuse, then `(n-1)` Exploding Kittens inserted after dealing. Each player still receives 1 Defuse + 5 non-EK cards.

##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|-------------------|-----------------|--------------|
| TC2 | `startNewGame(List.of("A", "B"))` | 2 alive players named `"A"`, `"B"`; each hand is 6 cards (1 Defuse + 5 non-EK from {See_The_Future, Attack, Shuffle}, no EK); pile has 1 EK and 4 Defuse; across hands+pile: 10 STF, 5 Attack, 5 Shuffle, 6 Defuse, 1 EK (rough deal mix; balance later) | yes |
| TC3 | `startNewGame(List.of("A", "B", "C", "D"))` | 4 alive players; each hand same shape as TC2; pile has 3 EK and 2 Defuse; across hands+pile: 10 STF, 5 Attack, 5 Shuffle, 6 Defuse, 3 EK | yes |
| TC4 | `startNewGame(List.of("A"))` | throws `IllegalArgumentException` with message `"gameManager.playerCount.outOfRange"` | yes |
| TC5 | `startNewGame(List.of("A", "B", "C", "D", "E"))` | throws `IllegalArgumentException` with message `"gameManager.playerCount.outOfRange"` | yes |
| TC6 | `startNewGame(List.of())` | throws `IllegalArgumentException` with message `"gameManager.playerCount.outOfRange"` | yes |
| TC7 | `startNewGame(List.of("", "B"))` | throws `IllegalArgumentException` with message `"gameManager.playerName.blank"` | yes |
| TC34 | `startNewGame(List.of("A", "B"))` again after `"A"` played Attack | `getDiscardPile()` is empty; `getTurnsRemaining()` is `1` | yes |

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
| TC73 | 3 players, `"B"` already eliminated, `"A"` holds Attack, `playAttack()` | current player is `"C"` (skips dead `"B"`); turns remaining is `2` | yes |

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

---

## Method 9: `public void playShuffle()`

##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|-------------------|-----------------|--------------|
| TC35 | `playShuffle()` before `startNewGame` | throws `IllegalStateException` with message `"gameManager.notStarted"` | yes |
| TC36 | 3 players, current player `"A"` has no Shuffle, `playShuffle()` | throws `IllegalStateException` with message `"gameManager.play.notInHand"`; hand, draw pile and discard pile unchanged | yes |
| TC37 | 2 players, `Random` always `0`, `"A"` holds 1 Shuffle, `playShuffle()` | Shuffle removed from `"A"`'s hand and is the last card of the discard pile; draw pile has the same 5 cards, rotated by one (old top card is now at the bottom) | yes |
| TC38 | 4 players, `Random` always `bound - 1` (`"D"` starts), `"D"` holds 1 Shuffle, `playShuffle()` | current player is still `"D"`; turns remaining is still `1` | yes |
| TC39 | 3 players, `"A"` holds 2 Shuffles, `playShuffle()` | exactly one Shuffle removed; one still in `"A"`'s hand | yes |
| TC40 | 2 players, `"A"` attacks `"B"`, `"B"` holds 1 Shuffle, `playShuffle()` | current player is still `"B"`; turns remaining is still `2` | yes |

---

## Method 10: `public List<Card> playSeeTheFuture()`

##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|-------------------|-----------------|--------------|
| TC41 | `playSeeTheFuture()` before `startNewGame` | throws `IllegalStateException` with message `"gameManager.notStarted"` | yes |
| TC42 | 3 players, current player `"A"` has no See the Future (all 5 removed), `playSeeTheFuture()` | throws `IllegalStateException` with message `"gameManager.play.notInHand"`; hand, draw pile and discard pile unchanged | yes |
| TC43 | 2 players, `"A"` holds 5 See the Future, `playSeeTheFuture()` | returns the top 3 cards of the draw pile, top first; draw pile unchanged (same 15 cards, same order); See the Future is the last card of the discard pile; `"A"` holds 4 See the Future | yes |
| TC44 | 4 players, `Random` always `bound - 1` (`"D"` starts), draw pile has 5 cards, `playSeeTheFuture()` | returns the top 3 of the 5 cards; current player is still `"D"`; turns remaining is still `1` | yes |
| TC45 | 2 players, `"A"` attacks `"B"`, `"B"` plays See the Future | current player is still `"B"`; turns remaining is still `2` | yes |
| TC46 | 2 players, `"A"` plays See the Future, then `.clear()` on the returned list | later `getDrawPile()` unchanged | yes |
| TC47 | draw pile has fewer than 3 cards, `playSeeTheFuture()` | CAN'T SET: no way to draw from the pile through `GameManager` yet; add once a draw method exists (expected: returns all remaining cards) | no |


---

## Method 11: `public Card drawCard()`

Ends the current player's turn by drawing the top card of the draw pile. A normal card goes into the hand and the turn ends. An Exploding Kitten stays in the hand if the player has a Defuse (the turn waits for the Defuse to be played). Without a Defuse, the player is eliminated, the Exploding Kitten goes to the discard pile, turns remaining reset to `1`, and the next **living** player becomes current (dead seats are skipped). After any turn advance, if the seated player is dead, skip to the next living seat.

##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|-------------------|-----------------|--------------|
| TC48 | `drawCard()` before `startNewGame` | throws `IllegalStateException` with message `"gameManager.notStarted"` | yes          |
| TC49 | 2 players, `"A"` starts, top card is See the Future, `drawCard()` | returns that card; it is in `"A"`'s hand; draw pile has one fewer card; current player is `"B"` | yes          |
| TC50 | 2 players, `"A"` attacks `"B"`, top card is See the Future, `"B"` draws | current player is still `"B"`; turns remaining is `1` | yes          |
| TC51 | 4 players, `"D"` starts, top card is See the Future, `drawCard()` | current player is `"A"` | yes          |
| TC52 | 2 players, `"A"` has 1 Defuse, top card is Exploding Kitten, `drawCard()` | Exploding Kitten is in `"A"`'s hand; `"A"` is alive; current player is still `"A"` | yes          |
| TC53 | 2 players, `"A"` has no Defuse, top card is Exploding Kitten, `drawCard()` | `"A"` is eliminated; Exploding Kitten is the last card of the discard pile and not in `"A"`'s hand; current player is `"B"`; turns remaining is `1` | yes |
| TC63 | 3 players, `"A"` starts, `"B"` already eliminated, `"A"` draws non-EK | current player is `"C"` (skips dead `"B"`) | yes |
| TC64 | 3 players, `"A"` starts with no Defuse, top is EK, `"B"` already eliminated | `"A"` eliminated; current player is `"C"`; turns remaining is `1` | yes |

---

## Method 12: `public void playDefuse(int reinsertIndex)`

Plays a Defuse after the current player has drawn an Exploding Kitten. The Defuse goes to the discard pile, the Exploding Kitten is put back into the draw pile at the chosen index (`0` = top, draw-pile size = bottom), and the turn ends the same way a normal draw does.

##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|-------------------|-----------------|--------------|
| TC54 | `playDefuse(0)` before `startNewGame` | throws `IllegalStateException` with message `"gameManager.notStarted"` | yes |
| TC55 | 2 players, current player `"A"` has Defuse but no Exploding Kitten, `playDefuse(0)` | throws `IllegalStateException` with message `"gameManager.defuse.noKitten"`; hand, draw pile and discard pile unchanged | yes |
| TC56 | 2 players, `"A"` draws Exploding Kitten with Defuse in hand, `playDefuse(0)` | Defuse is the last card of the discard pile; Exploding Kitten is not in `"A"`'s hand and is the top card of the draw pile; current player is `"B"` | yes |
| TC57 | 2 players, `"A"` draws Exploding Kitten with Defuse in hand, `playDefuse` at bottom (index = draw-pile size) | Exploding Kitten is the bottom card of the draw pile; Defuse is on the discard pile; current player is `"B"` | yes |
| TC58 | 2 players, `"A"` attacks `"B"`, `"B"` draws Exploding Kitten with Defuse and `playDefuse(0)` | current player is still `"B"`; turns remaining is `1` | yes |
| TC59 | 4 players, `"D"` starts, `"D"` draws Exploding Kitten with Defuse and `playDefuse(0)` | current player is `"A"` | yes |
| TC60 | 2 players, `"A"` holds 2 Defuses and draws Exploding Kitten, `playDefuse(0)` | exactly one Defuse removed from `"A"`'s hand and on the discard pile; one Defuse still in hand | yes |
| TC61 | 2 players, `"A"` draws Exploding Kitten with Defuse, `playDefuse(-1)` or index past bottom | throws `IllegalArgumentException`; Exploding Kitten and Defuse still in `"A"`'s hand; discard pile unchanged | yes |
| TC62 | 2 players, `"A"` draws Exploding Kitten with Defuse, then Defuse is removed from hand, `playDefuse(0)` | throws `IllegalStateException` with message `"gameManager.play.notInHand"`; Exploding Kitten still in `"A"`'s hand | yes |

---

## Method 13: `public boolean isGameOver()`

True when at most one player is still alive. Extensible later for an alternate win condition without changing the UI loop.

##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|-------------------|-----------------|--------------|
| TC65 | `isGameOver()` before `startNewGame` | throws `IllegalStateException` with message `"gameManager.notStarted"` | no |
| TC66 | after `startNewGame` with 2 players, both alive | `false` | no |
| TC67 | 2 players, `"A"` eliminated by EK without Defuse | `true` | no |
| TC68 | 3 players, only `"A"` eliminated, `"B"` and `"C"` alive | `false` | no |

---

## Method 14: `public Player getWinner()`

Returns the sole remaining living player. Throws if the game is not over (keeps alt-win flexibility: UI only calls this when `isGameOver()` is true).

##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|-------------------|-----------------|--------------|
| TC69 | `getWinner()` before `startNewGame` | throws `IllegalStateException` with message `"gameManager.notStarted"` | no |
| TC70 | after start, both alive | throws `IllegalStateException` with message `"gameManager.notOver"` | no |
| TC71 | 2 players, `"A"` eliminated | returns `"B"` | no |
| TC72 | 3 players, `"A"` and `"B"` eliminated | returns `"C"` | no |