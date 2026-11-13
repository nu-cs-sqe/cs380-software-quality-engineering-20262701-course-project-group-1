package domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * An ordered pile of cards. Index 0 is the top of the deck.
 */
public class Deck {
    private final List<Card> cards;

    private final Random random;

    /**
     * Creates a full 56-card deck.
     */
    public Deck() {
        this.random = new Random();
        cards = new ArrayList<>();
        cards.add(new Card(CardType.Exploding_Kitten));
        for (int i = 0; i < 55; i++) {
            cards.add(new Card(CardType.Defuse));
        }
    }

    // DeckTest Constructors
    Deck(List<Card> cards) {
        this(cards, new Random());
    }

    Deck(List<Card> cards, Random random) {
        this.cards = new ArrayList<>(cards);
        this.random = random;
    }

    public int getDeckSize() {
        return cards.size();
    }

    // Used in DeckTests to ensure proper Deck State
    Card getCardAt(int index) {
        return cards.get(index);
    }

    /**
     * Removes and returns the top card of the deck.
     *
     * @return the top card
     * @throws IllegalStateException if the deck is empty
     */
    public Card drawCard() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("Cannot draw from an empty deck");
        }
        return cards.remove(0);
    }

    /**
     * Shuffles the deck using this deck's {@link Random}.
     */
    public void shuffleDeck() {
        Collections.shuffle(cards, random);
    }

    /**
     * Returns the top cards of the deck without removing them.
     *
     * @param numberOfCards how many cards to look at
     * @return a copy of the top {@code numberOfCards} cards, top card first
     * @throws IllegalArgumentException if {@code numberOfCards} is negative
     * @throws IllegalStateException if the deck is empty or has fewer than {@code numberOfCards}
     *     cards
     */
    public List<Card> peekTopofDeck(int numberOfCards) {
        if (numberOfCards < 0) {
            throw new IllegalArgumentException("Cannot peek at a negative number of cards");
        }
        if (cards.isEmpty()) {
            throw new IllegalStateException("Cannot peek at an empty deck");
        }

        if (numberOfCards > cards.size()) {
            throw new IllegalStateException(
                    "Not enough cards in the deck to peek at" + numberOfCards);
        }

        return new ArrayList<>(cards.subList(0, numberOfCards));
    }

    /**
     * Inserts a card at the given position, where 0 is the top and the deck size is the bottom.
     *
     * @param card the card to insert
     * @param index the position to insert at, from 0 to the deck size
     * @throws IllegalArgumentException if {@code card} is null or {@code index} is out of range
     */
    public void insertCardAt(Card card, int index) {
        if (card == null) {
            throw new IllegalArgumentException("Card cannot be null");
        }
        if (index < 0 || index > cards.size()) {
            throw new IllegalArgumentException("Index out of range: " + index);
        }
        cards.add(index, card);
    }

    /**
     * Removes every card of the given type from the deck.
     *
     * @param type the type of card to remove
     * @return the removed cards, in the order they were in the deck
     * @throws IllegalArgumentException if {@code type} is null
     */
    public List<Card> removeAllCardsofType(CardType type) {
        if (type == null) {
            throw new IllegalArgumentException("Card type cannot be null");
        }

        List<Card> removed = new ArrayList<>();
        Iterator<Card> iterator = cards.iterator();
        while (iterator.hasNext()) {
            Card card = iterator.next();
            if (card.getTypeofCard() == type) {
                removed.add(card);
                iterator.remove();
            }
        }
        return removed;
    }

    /**
     * Adds a card to the bottom of the deck.
     *
     * @param card the card to add
     * @throws IllegalArgumentException if {@code card} is null
     */
    public void addCard(Card card) {
        insertCardAt(card, cards.size());
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }
}
