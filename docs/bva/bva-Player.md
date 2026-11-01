# BVA Analysis for `Player`

This file holds the BVA analysis for every public method of the `Player` class. Each public method has its own `## Method N:` section; new methods append a new section as the class grows.

Made with assistance from GenAI.

---

## Method 1: `public Player(String name)`

##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|-------------------|-----------------|--------------|
| TC1 | `new Player("A")` | `getName()` is `"A"`; `isAlive()` is `true`; `getHand()` is empty | yes |

---

## Method 2: `public String getName()`

##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|-------------------|-----------------|--------------|
| TC2 | `new Player("A").getName()` | `"A"` | yes (implemented in TC1) |
| TC3 | `new Player("B").getName()` | `"B"` | yes |

---

## Method 3: `public boolean isAlive()`

##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|-------------------|-----------------|--------------|
| TC4 | `new Player("A").isAlive()` | `true` | yes (implemented in TC1) |

---

## Method 4: `public void addCard(Card card)`

##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|-------------------|-----------------|--------------|
| TC5 | `addCard(new Card(CardType.See_The_Future))` on empty hand | `getHand()` size is `1` | yes |

---

## Method 5: `public List<Card> getHand()`

##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|-------------------|-----------------|--------------|
| TC6 | `new Player("A").getHand()` | empty list | yes (implemented in TC1) |
| TC7 | `getHand()` after one `addCard` | size `1` | yes (implemented in TC5) |
| TC8 | `getHand().clear()` after one `addCard` | later `getHand()` size unchanged | no |
