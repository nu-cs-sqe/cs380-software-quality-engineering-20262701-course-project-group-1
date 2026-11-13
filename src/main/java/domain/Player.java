package domain;

import java.util.ArrayList;
import java.util.List;

/**
 * A player in the game, with a name and a hand of cards.
 */
public class Player {

    private static final String CARD_NOT_IN_HAND = "player.card.notInHand";
    private final String name;
    private final List<Card> hand = new ArrayList<>();

    /**
     * Creates a living player with an empty hand.
     *
     * @param name the player's name
     */
    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isAlive() {
        return true;
    }

    /**
     * Adds a card to the end of this player's hand.
     *
     * @param card the card to add
     */
    public void addCard(Card card) {
        hand.add(card);
    }

    public List<Card> getHand() {
        return new ArrayList<>(hand);
    }

    /**
     * Removes and returns the first card of the given type in this player's hand.
     *
     * @param type the type of card to remove
     * @return the removed card
     * @throws IllegalStateException if the hand has no card of that type
     */
    public Card removeCardOfType(CardType type) {
        for (int i = 0; i < hand.size(); i++) {
            if (hand.get(i).getTypeofCard() == type) {
                return hand.remove(i);
            }
        }
        throw new IllegalStateException(CARD_NOT_IN_HAND);
    }
}
