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

Starting cards: 10 See_The_Future, 5 Attack, 5 Shuffle, 4 Gamble_Cat, 6 Defuse, then (n-1) Exploding Kittens and one of each of the four special cats (Special_Cat_1 to Special_Cat_4) inserted after dealing, so the special cats are never in a starting hand. Each player still receives 1 Defuse + 5 non-EK cards.

##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|-------------------|-----------------|--------------|
| TC2 | `startNewGame(List.of("A", "B"))` | 2 alive players named `"A"`, `"B"`; each hand is 6 cards (1 Defuse + 5 non-EK from {See_The_Future, Attack, Shuffle, Gamble_Cat}, no EK); pile has 1 EK and 4 Defuse; across hands+pile: 10 STF, 5 Attack, 5 Shuffle, 4 Gamble_Cat, 6 Defuse, 1 EK | yes |
| TC3 | `startNewGame(List.of("A", "B", "C", "D"))` | 4 alive players; each hand same shape as TC2; pile has 3 EK and 2 Defuse; across hands+pile: 10 STF, 5 Attack, 5 Shuffle, 4 Gamble_Cat, 6 Defuse, 3 EK | yes |
| TC4 | `startNewGame(List.of("A"))` | throws `IllegalArgumentException` with message `"gameManager.playerCount.outOfRange"` | yes |
| TC5 | `startNewGame(List.of("A", "B", "C", "D", "E"))` | throws `IllegalArgumentException` with message `"gameManager.playerCount.outOfRange"` | yes |
| TC6 | `startNewGame(List.of())` | throws `IllegalArgumentException` with message `"gameManager.playerCount.outOfRange"` | yes |
| TC7 | `startNewGame(List.of("", "B"))` | throws `IllegalArgumentException` with message `"gameManager.playerName.blank"` | yes |
| TC34 | `startNewGame(List.of("A", "B"))` again after `"A"` played Attack | `getDiscardPile()` is empty; `getTurnsRemaining()` is `1` | yes |
| TC107 | startNewGame(List.of("A", "B")) | draw pile has exactly one each of Special_Cat_1, Special_Cat_2, Special_Cat_3, Special_Cat_4; no hand has any special cat | yes |
| TC108 | startNewGame(List.of("A", "B", "C", "D")) | draw pile has 4 special cats; no hand has any special cat | yes |

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
| TC20 | `playAttack()` before `startNewGame` | throws `IllegalStateException` with message `"gameManager.notStarted"` | yes          |
| TC21 | 3 players, current player `"A"` has no Attack, `playAttack()` | throws `IllegalStateException` with message `"gameManager.play.notInHand"`; hand and current player unchanged | yes          |
| TC22 | 2 players, `"A"` holds 1 Attack, `playAttack()` | Attack removed from `"A"`'s hand and on top of discard pile; current player is `"B"`; turns remaining is `2` | yes          |
| TC23 | 4 players, `Random` always `0` (`"A"` starts), `"A"` holds 1 Attack, `playAttack()` | current player is `"B"` (next seat, not `"C"` or `"D"`); turns remaining is `2` | yes          |
| TC24 | 4 players, `Random` always `bound - 1` (`"D"` starts), `"D"` holds 1 Attack, `playAttack()` | current player is `"A"` (wraps from last seat); turns remaining is `2` | yes          |
| TC25 | 3 players, `"A"` holds 2 Attacks, `playAttack()` | exactly one Attack removed; one still in `"A"`'s hand | yes          |
| TC26 | 3 players, `"A"` attacks `"B"`, then `"B"` plays Attack | current player is `"C"` (not back to `"A"`); turns remaining is `4` | yes          |
| TC73 | 3 players, `"B"` eliminated earlier by drawing an Exploding Kitten with no Defuse, back to `"A"`, `"A"` holds Attack, `playAttack()` | current player is `"C"` (skips dead `"B"`); turns remaining is `2` | yes          |
| TC125 | 2 players, `"A"` holds Attack, `"B"` plays a Nope, then `playAttack()` | Attack is on the discard pile and not in `"A"`'s hand; current player is still `"A"`; turns remaining is `1`; `isNoped()` is `false` | yes          |
| TC126 | 2 players, `"A"` holds Attack, `"B"` plays a Nope, `"A"` plays a Nope back, then `playAttack()` | the Attack happens: current player is `"B"`; turns remaining is `2`; `isNoped()` is `false` | yes          |
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
| TC35 | `playShuffle()` before `startNewGame` | throws `IllegalStateException` with message `"gameManager.notStarted"` | yes          |
| TC36 | 3 players, current player `"A"` has no Shuffle, `playShuffle()` | throws `IllegalStateException` with message `"gameManager.play.notInHand"`; hand, draw pile and discard pile unchanged | yes          |
| TC37 | 2 players, `Random` always `0`, `"A"` holds 1 Shuffle, `playShuffle()` | Shuffle removed from `"A"`'s hand and is the last card of the discard pile; draw pile has the same 5 cards, rotated by one (old top card is now at the bottom) | yes          |
| TC38 | 4 players, `Random` always `bound - 1` (`"D"` starts), `"D"` holds 1 Shuffle, `playShuffle()` | current player is still `"D"`; turns remaining is still `1` | yes          |
| TC39 | 3 players, `"A"` holds 2 Shuffles, `playShuffle()` | exactly one Shuffle removed; one still in `"A"`'s hand | yes          |
| TC40 | 2 players, `"A"` attacks `"B"`, `"B"` holds 1 Shuffle, `playShuffle()` | current player is still `"B"`; turns remaining is still `2` | yes          |
| TC127 | 2 players, `"A"` holds Shuffle, `"B"` plays a Nope, then `playShuffle()` | draw pile order unchanged; Shuffle is on the discard pile; `isNoped()` is `false` | yes          |
---

## Method 10: `public List<Card> playSeeTheFuture()`

##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|-------------------|-----------------|--------------|
| TC41 | `playSeeTheFuture()` before `startNewGame` | throws `IllegalStateException` with message `"gameManager.notStarted"` | yes          |
| TC42 | 3 players, current player `"A"` has no See the Future (all 5 removed), `playSeeTheFuture()` | throws `IllegalStateException` with message `"gameManager.play.notInHand"`; hand, draw pile and discard pile unchanged | yes          |
| TC43 | 2 players, `"A"` holds 5 See the Future, `playSeeTheFuture()` | returns the top 3 cards of the draw pile, top first; draw pile unchanged (same 15 cards, same order); See the Future is the last card of the discard pile; `"A"` holds 4 See the Future | yes          |
| TC44 | 4 players, `Random` always `bound - 1` (`"D"` starts), draw pile has 13 cards (9 plus the 4 special cats), `playSeeTheFuture()` | returns the top 3 of the 13 cards; current player is still `"D"`; turns remaining is still `1` | yes          |
| TC45 | 2 players, `"A"` attacks `"B"`, `"B"` plays See the Future | current player is still `"B"`; turns remaining is still `2` | yes          |
| TC46 | 2 players, `"A"` plays See the Future, then `.clear()` on the returned list | later `getDrawPile()` unchanged | yes          |
| TC47 | draw pile has fewer than 3 cards, `playSeeTheFuture()` | CAN'T SET: no way to draw from the pile through `GameManager` yet; add once a draw method exists (expected: returns all remaining cards) | no           |
| TC128 | 2 players, `"A"` holds See the Future, `"B"` plays a Nope, then `playSeeTheFuture()` | returns an empty list; See the Future is on the discard pile; `isNoped()` is `false` | yes          |

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
| TC63 | 3 players, `"A"` starts, `"B"` eliminated earlier by drawing an Exploding Kitten with no Defuse, back to `"A"`, `"A"` draws non-EK | current player is `"C"` (skips dead `"B"`) | yes |
| TC64 | 3 players, `"B"` eliminated earlier by drawing an Exploding Kitten with no Defuse, back to `"A"`; `"A"` has no Defuse, top is EK | `"A"` eliminated; current player is `"C"`; turns remaining is `1` | yes |

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

## Method 13: `public boolean playGambleCat()`

Custom card. Plays a Gamble Cat from the current player's hand, then picks an outcome at random (50/50, `random.nextInt(2)`): `0` = lucky, the turn ends immediately without drawing (like Skip); `1` = unlucky, the current turn only ends after two draws in a row, and no cards can be played between them. A defused Exploding Kitten counts as one of the two draws. Returns `true` if the turn ended.

##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|-------------------|-----------------|--------------|
| TC78 | `playGambleCat()` before `startNewGame` | throws `IllegalStateException` with message `"gameManager.notStarted"` | yes |
| TC79 | 3 players, current player `"A"` has no Gamble Cat, `playGambleCat()` | throws `IllegalStateException` with message `"gameManager.play.notInHand"`; hand, draw pile, discard pile and current player unchanged | yes |
| TC80 | 2 players, `Random` always `0` (`"A"` starts, lucky), `playGambleCat()` | returns `true`; Gamble Cat is the last card of the discard pile; current player is `"B"`; draw pile unchanged | yes |
| TC81 | 4 players, `Random` always `0` (`"A"` starts, lucky), `playGambleCat()` | returns `true`; current player is `"B"` (next seat, not `"C"` or `"D"`) | yes |
| TC82 | 4 players, `Random` always `bound - 1` (`"D"` starts, unlucky), `playGambleCat()` | returns `false`; current player is still `"D"`; turns remaining `1`; draw pile unchanged | yes |
| TC83 | 4 players, `"D"` unlucky, then `drawCard()` twice (top two See the Future) | after first draw: still `"D"`; after second draw: current player is `"A"` (wraps) | yes |
| TC84 | 2 players, `"B"` unlucky and holds an Attack, `drawCard()` once, then `playAttack()` | throws `IllegalStateException` with message `"gameManager.play.mustFinishDrawing"`; still `"B"`; Attack still in hand | yes |
| TC85 | 2 players, `"B"` unlucky, first draw Exploding Kitten with Defuse, `playDefuse(<bottom index>)`, then `drawCard()` (See the Future) | after Defuse: still `"B"`; after the second draw: current player is `"A"` | yes |
| TC86 | 2 players, `Random` always `0`, `"A"` attacks `"B"`, `"B"` plays Gamble Cat (lucky) | returns `true`; still `"B"`; turns remaining `1` (cancels one owed turn) | yes |
| TC87 | 2 players, `Random` always `bound - 1`, `"B"` attacks `"A"`, `"A"` plays Gamble Cat (unlucky), then `drawCard()` twice | still `"A"`; turns remaining `1` | yes |
| TC88 | 2 players, `"B"` unlucky, then `playAttack()` before drawing | allowed; current player is `"A"`; turns remaining `2`; one `drawCard()` by `"A"` leaves `"A"` owing 1 (extra draw not passed on) | yes |
| TC89 | 3 players, `"A"` holds 2 Gamble Cats, `playGambleCat()` | exactly one Gamble Cat removed; one still in `"A"`'s hand | yes |
| TC90 | 2 players, `Random` is an EasyMock mock: setup returns `0` (`"A"` starts), then exactly one `nextInt(2)` returning `1`, `playGambleCat()` | returns `false`; current player is still `"A"`; mock verifies exactly one `nextInt(2)` call | yes |
| TC91 | 2 players, `Random` is an EasyMock mock: setup returns `0` (`"A"` starts), then exactly one `nextInt(2)` returning `0`, `playGambleCat()` | returns `true`; current player is `"B"`; mock verifies exactly one `nextInt(2)` call | yes |
| TC92 | 2 players, `"B"` unlucky, first draw See the Future, second draw Exploding Kitten with Defuse, `playDefuse(bottom)` | Defuse allowed mid-draw; Exploding Kitten is the bottom card of the draw pile; current player is `"A"` | yes |
| TC129 | 2 players, `"A"` holds Gamble Cat, `Random` would give lucky, `"B"` plays a Nope, then `playGambleCat()` | returns `false`; current player is still `"A"`; then one `drawCard()` makes `"B"` current (no extra draw); `isNoped()` is `false` | no |
---

## Method 14: `public boolean isGameOver()`

True when at most one player is still alive. Extensible later for an alternate win condition without changing the UI loop.

##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|-------------------|-----------------|--------------|
| TC93 | `isGameOver()` before `startNewGame` | throws `IllegalStateException` with message `"gameManager.notStarted"` | yes |
| TC94 | after `startNewGame` with 2 players, both alive | `false` | yes |
| TC95 | 2 players, `"A"` eliminated by EK without Defuse | `true` | yes |
| TC96 | 3 players, only `"A"` eliminated, `"B"` and `"C"` alive | `false` | yes |

---

## Method 15: `public Player getWinner()`

Returns the sole remaining living player. Throws if the game is not over (keeps alt-win flexibility: UI only calls this when `isGameOver()` is true).

##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|-------------------|-----------------|--------------|
| TC97 | `getWinner()` before `startNewGame` | throws `IllegalStateException` with message `"gameManager.notStarted"` | yes |
| TC98 | after start, both alive | throws `IllegalStateException` with message `"gameManager.notOver"` | yes |
| TC99 | 2 players, `"A"` eliminated | returns `"B"` | yes |
| TC100 | 3 players, `"A"` and `"B"` eliminated | returns `"C"` | yes |

## Method 16: `public int getDrawPileSize()`

| Test Case # | System under test | Expected output | Implemented? |
|-------------|-------------------|-----------------|--------------|
| TC104 | `getDrawPileSize()` before `startNewGame` | throws `IllegalStateException` with message `"gameManager.notStarted"` | yes |
| TC105 | after `startNewGame` with 4 players | equals `getDrawPile().size()` | yes |
| TC106 | 2 players, See the Future on top, `drawCard()` | one less than before the draw | yes |

---

## Method 17: `public Player playSpecialCatSet()`

Alternate win. The current player plays all four special cats at once; the only other living player is eliminated and the current player wins. Only allowed when exactly two players are alive. The four cats go to the discard pile. Like other card plays, it is rejected between the two draws of an unlucky Gamble Cat.

##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|-------------------|-----------------|--------------|
| TC109 | 2 players, `"A"` current, holds all four special cats, `playSpecialCatSet()` | returns `"B"`; `"B"` eliminated; `isGameOver()` is `true`; winner is `"A"`; the 4 cats are in the discard pile and not in `"A"`'s hand | yes |
| TC110 | playSpecialCatSet() before startNewGame | throws IllegalStateException with message "gameManager.notStarted" | yes |
| TC111 | 3 players all alive, `"A"` holds all four, `playSpecialCatSet()` | throws `IllegalStateException` with message `"gameManager.catSet.needTwoPlayers"`; hand and discard pile unchanged; game not over | yes |
| TC112 | 2 players, `"A"` holds only 3 of the 4 special cats, `playSpecialCatSet()` | throws `IllegalStateException` with message `"gameManager.catSet.incomplete"`; hand and discard pile unchanged; `"B"` alive | yes |
| TC113 | 3 players, `"B"` eliminated earlier by an Exploding Kitten, back to `"A"`, `"A"` holds all four, `playSpecialCatSet()` | returns `"C"`; game over; winner is `"A"` (counts living players, not seats) | yes |
| TC114 | 4 players all alive, `"A"` holds all four, `playSpecialCatSet()` | throws `IllegalStateException` with message `"gameManager.catSet.needTwoPlayers"` | yes |
| TC115 | 2 players, `"B"` unlucky Gamble Cat, has drawn once, holds all four, `playSpecialCatSet()` | throws `IllegalStateException` with message `"gameManager.play.mustFinishDrawing"`; hand unchanged; `"A"` alive | yes |

---

## Method 18: `public void playNope(Player player)`

Any living player (not only the current player) plays a Nope from their hand. The Nope goes to the discard pile and the "noped" flag flips: one Nope stops the action that is about to happen, a second Nope stops the first Nope, and so on. A Nope cannot be played between the two draws of an unlucky Gamble Cat, since there is no action to stop.

##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|-------------------|-----------------|--------------|
| TC116 | `playNope(player)` before `startNewGame` | throws `IllegalStateException` with message `"gameManager.notStarted"` | yes          |
| TC117 | 2 players, `"A"` current, `"B"` has no Nope, `playNope(B)` | throws `IllegalStateException` with message `"gameManager.play.notInHand"`; discard pile unchanged; `isNoped()` is `false` | yes          |
| TC118 | 2 players, `"A"` current, `"B"` holds 1 Nope, `playNope(B)` | Nope is the last card of the discard pile and not in `"B"`'s hand; `isNoped()` is `true`; current player is still `"A"`; turns remaining is `1` | yes          |
| TC119 | 2 players, `"B"` holds 2 Nopes, `playNope(B)` | exactly one Nope on the discard pile; one Nope still in `"B"`'s hand | yes          |
| TC120 | 2 players, `"B"` and `"A"` each hold 1 Nope, `playNope(B)` then `playNope(A)` | `isNoped()` is `false` (a Nope on a Nope); both Nopes on the discard pile | yes          |
| TC121 | 3 players, `"B"` eliminated earlier, `"B"` holds a Nope, `playNope(B)` | throws `IllegalStateException` with message `"gameManager.nope.eliminated"`; Nope still in `"B"`'s hand; `isNoped()` is `false` | yes          |
| TC122 | 2 players, `"B"` unlucky Gamble Cat and has drawn once, `"A"` holds a Nope, `playNope(A)` | throws `IllegalStateException` with message `"gameManager.play.mustFinishDrawing"`; Nope still in `"A"`'s hand | yes          |

---

## Method 19: `public boolean isNoped()`

Returns whether the action about to happen has been stopped by a Nope.

##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|-------------------|-----------------|--------------|
| TC123 | 2 players, right after `startNewGame` | `false` | yes          |
| TC124 | 2 players, `"B"` plays a Nope, then `startNewGame` again | `false` (a new game clears the flag) | yes          |
| — | after one Nope / after a Nope on a Nope | see TC118 / TC120 | —            |