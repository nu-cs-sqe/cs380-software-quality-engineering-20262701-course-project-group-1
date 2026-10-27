package domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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


}