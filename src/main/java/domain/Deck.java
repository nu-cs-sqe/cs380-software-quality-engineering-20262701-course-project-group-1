package domain;

import java.util.ArrayList;
import java.util.List;

public class Deck {
    private final List<Card> cards;

    public Deck() {
        cards = new ArrayList<>();
        cards.add(new Card(CardType.Exploding_Kitten));
        for (int i = 0; i < 55; i++) {
            cards.add(new Card(CardType.Defuse));
        }
    }

    Deck(List<Card> cards) {
        this.cards = new ArrayList<>(cards);
    }

    public int getDeckSize() {
        return cards.size();
    }

    Card getCardAt(int index) {
        return cards.get(index);
    }

    public Card drawCard() {
        throw new IllegalStateException("Cannot draw from an empty deck");
    }
}