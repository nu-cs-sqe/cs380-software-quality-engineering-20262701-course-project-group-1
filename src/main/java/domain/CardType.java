package domain;

/**
 * The kinds of cards that can appear in the game.
 *
 * <p>{@link #toString()} is the name shown to players. The four special cats have their display
 * names here, so renaming one only means changing its string.
 */
public enum CardType {
    Defuse,
    Exploding_Kitten,
    See_The_Future,
    Attack,
    Shuffle,
    Gamble_Cat,
    Special_Cat_1("Fire Cat"),
    Special_Cat_2("Water Cat"),
    Special_Cat_3("Earth Cat"),
    Special_Cat_4("Air Cat");

    private final String displayName;

    CardType() {
        this.displayName = name();
    }

    CardType(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}