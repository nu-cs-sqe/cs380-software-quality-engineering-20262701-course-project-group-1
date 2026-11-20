# BVA Analysis for `GameLoop`

This file holds the BVA analysis for every public method of the `GameLoop` class. Each public method has its own `## Method N:` section; new methods append a new section as the class grows.

Drives a terminal session: reads from a `Scanner`, prints via `TerminalPrinting`, mutates state through `GameManager`. 

Made with assistance from GenAI.

---

## Method 1: `public GameLoop(GameManager gameManager, TerminalPrinting printer, Scanner in)`

##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|-------------------|-----------------|--------------|
| TC1 | `new GameLoop(manager, printer, scanner)` | constructs without exception | yes |

---

## Method 2: `public void run()`

Full interactive session. Tests feed scripted `Scanner` input and assert on printer/`GameManager` outcomes (not a real keyboard).

##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|-------------------|-----------------|--------------|
| TC2 | input names `"A"`, `"B"` then players only draw until one eliminates (scripted top cards via test hooks / enough draws) | game ends; winner printed; `isGameOver()` true | yes |
| TC3 | names `"A"` only (then quit / invalid count path) | rejects player count outside 2–4; does not call a successful `startNewGame` with 1 player | yes |
| TC4 | names `"A"`, `"B"`, `"C"`, `"D"` | `startNewGame` with 4 players; setup printed | yes |
| TC5 | blank name among otherwise valid names | rejects blank name; game not started with that list | yes |
| TC6 | current player chooses hand index of Attack | `playAttack` effect; turn may move; next turn banner printed | yes |
| TC7 | current player chooses hand index of Shuffle | `playShuffle`; same player still current | yes |
| TC8 | current player chooses hand index of See_The_Future | `playSeeTheFuture`; peek printed via `printSeeTheFuture`; turn unchanged | yes |
| TC9 | current player chooses Draw | `drawCard`; if non-EK, turn ends per domain rules | yes |
| TC10 | draw Exploding Kitten with Defuse in hand, then valid reinsert index `0` | `playDefuse(0)`; kitten on top of draw pile | yes |
| TC11 | draw Exploding Kitten with Defuse, reinsert at bottom (index = draw pile size) | kitten at bottom; turn ends per domain | yes |
| TC12 | draw Exploding Kitten with no Defuse | player eliminated printed; turn advances to next living; if one left, winner printed | yes |
| TC13 | play Defuse from hand menu while no kitten pending | illegal; error message printed; hand unchanged | yes |
| TC14 | play index out of range / non-integer | error message; hand and turn unchanged | yes |
| TC15 | after a turn ends and next player differs | hotseat pause prompt; previous hand not required to stay on screen (dev: at least a pass/Enter gate) | yes |
| TC16 | 2 players, `"A"` starts, plays Gamble Cat by index, flip is lucky | prints the lucky line; current player is `"B"`; hand-off prompt for `"B"` shown | yes |
| TC17 | 2 players, `"B"` starts, plays Gamble Cat by index, flip is unlucky, then draws twice (See the Future on top) | prints the unlucky line; still `"B"` after the first draw; current player is `"A"` after the second; hand-off prompt shown exactly once | yes |
| TC18 | 2 players, `"B"` unlucky, draws once, then tries to play Attack by index | play rejected; still `"B"`; Attack still in hand | yes |
