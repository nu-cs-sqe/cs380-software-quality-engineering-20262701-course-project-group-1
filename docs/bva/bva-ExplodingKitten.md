# BVA Analysis for `ExplodingKitten`

This file holds the BVA analysis for every public method of the `ExplodingKitten` class. Each public method has its own `## Method N:` section; new methods append a new section as the class grows.

Made with assistance from GenAI.

---

## Method 1: `public void resolve(Player player, Deck drawPile, int insertIndex)`

Called when `player` has just drawn an Exploding Kitten (EK). If the player has a Defuse, one Defuse is removed from their hand and the EK is put back into `drawPile` at `insertIndex`. Otherwise the player is eliminated and the EK is not put back.

|        | Input | Output |
|--------|-------|--------|
| Step 1 | `player`'s hand; `drawPile`; `insertIndex` | Player alive with one fewer Defuse and EK at `insertIndex`, OR player eliminated, OR exception |
| Step 2 | `insertIndex`: Interval `[0, drawPile size]`; number of Defuse in hand: Count; `drawPile`: Collection | Cases: alive / eliminated / `IllegalArgumentException` |
| Step 3 | `insertIndex`: -1, 0, drawPile size, drawPile size + 1; Defuse count: 0, 1, >1 (2); `drawPile`: empty, more than one element (3 cards) | See test cases below |

##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|-------------------|-----------------|--------------|
| TC1 | Hand: 1 Defuse + 2 See_The_Future; pile has 3 cards; `resolve(player, pile, 0)` | Player alive; hand is 2 See_The_Future; pile has 4 cards; card at index 0 is EK | no |
| TC2 | Hand: 1 Defuse + 2 See_The_Future; pile has 3 cards; `resolve(player, pile, 3)` | Player alive; hand is 2 See_The_Future; pile has 4 cards; card at index 3 (bottom) is EK | no |
| TC3 | Hand: 2 Defuse; pile has 3 cards; `resolve(player, pile, 1)` | Player alive; hand has exactly 1 Defuse; pile has 4 cards; card at index 1 is EK | no |
| TC4 | Hand: 1 Defuse; pile is empty; `resolve(player, pile, 0)` | Player alive; hand is empty; pile has 1 card and it is EK | no |
| TC5 | Hand: 1 Defuse; pile has 3 cards; `resolve(player, pile, -1)` | throws `IllegalArgumentException` with message `"explodingKitten.insertIndex.outOfRange"`; player alive; hand still has 1 Defuse; pile unchanged (3 cards) | no |
| TC6 | Hand: 1 Defuse; pile has 3 cards; `resolve(player, pile, 4)` | throws `IllegalArgumentException` with message `"explodingKitten.insertIndex.outOfRange"`; player alive; hand still has 1 Defuse; pile unchanged (3 cards) | no |
| TC7 | Hand: 2 See_The_Future, no Defuse; pile has 3 cards; `resolve(player, pile, 0)` | `player.isAlive()` is `false`; pile unchanged (3 cards, no EK added) | no |
| TC8 | Hand: 1 See_The_Future, no Defuse; pile has 3 cards; `resolve(player, pile, -1)` | `player.isAlive()` is `false`; no exception (`insertIndex` is ignored without a Defuse); pile unchanged | no |