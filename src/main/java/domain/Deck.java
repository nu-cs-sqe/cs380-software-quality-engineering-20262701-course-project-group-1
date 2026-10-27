package domain;

import java.util.*;

public class Deck {
    private final List<Card> cards;

    private final Random random;

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

    public Card drawCard() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("Cannot draw from an empty deck");
        }
        return cards.remove(0);
    }

    public void shuffleDeck() {
        Collections.shuffle(cards, random);
    }

    public List<Card> peekTopofDeck(int numberOfCards) {
        if (numberOfCards < 0) {
            throw new IllegalArgumentException("Cannot peek at a negative number of cards");
        }
        if (cards.isEmpty()) {
            throw new IllegalStateException("Cannot peek at an empty deck");
        }

        if (numberOfCards > cards.size()) {
            throw new IllegalStateException("Not enough cards in the deck to peek at" + numberOfCards);
        }

        return new ArrayList<>(cards.subList(0, numberOfCards));
    }

    public void insertCardAt(Card card, int index) {
        if (card == null) {
            throw new IllegalArgumentException("Card cannot be null");
        }
        if (index < 0 || index > cards.size()) {
            throw new IllegalArgumentException("Index out of range: " + index);
        }
        cards.add(index, card);
    }

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

    public void addCard(Card card) {
        insertCardAt(card, cards.size());
    }

    public boolean isEmpty() {
        return true;
    }
}