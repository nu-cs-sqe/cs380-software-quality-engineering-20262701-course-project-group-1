package domain;

/**
 * A single card in the game, identified by its {@link CardType}.
 */
public final class Card {
    private final CardType type;

    /**
     * Creates a card of the given type.
     *
     * @param type the type of the card
     * @throws IllegalArgumentException if {@code type} is null
     */
    public Card(CardType type) {
        if (type == null) {
            throw new IllegalArgumentException("Not a valid Card Type, Card Type cannot be null");
        }
        this.type = type;
    }

    public CardType getTypeofCard() {
        return type;
    }
}
