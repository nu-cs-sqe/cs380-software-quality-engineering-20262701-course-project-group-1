# BVA Analysis for `CardType`

`CardType` is an enum, but it now has behavior: `toString()` is the name shown to players. The four special cats have display names; every other card shows its constant name. To rename a special cat, change only its string in `CardType` and the matching row below.

---

## Method 1: `public String toString()`

##### All-combination or each-choice: each-choice

| Test Case # | System under test | Expected output | Implemented? |
|-------------|-------------------|-----------------|--------------|
| TC1 | `CardType.Special_Cat_1.toString()` | `"Fire Cat"` | no |
| TC2 | `CardType.Special_Cat_2.toString()` | `"Water Cat"` | no |
| TC3 | `CardType.Special_Cat_3.toString()` | `"Earth Cat"` | no |
| TC4 | `CardType.Special_Cat_4.toString()` | `"Air Cat"` | no |
| TC5 | `CardType.See_The_Future.toString()` (a card with no display name) | `"See_The_Future"` (the constant name, unchanged) | no |