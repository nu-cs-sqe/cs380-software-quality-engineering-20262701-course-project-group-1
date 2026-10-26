package domain;

import java.util.ArrayList;
import java.util.List;

public class Deck {
    private final List<Card> cards = new ArrayList<>();

    public Deck() {
        cards.add(new Card(CardType.Exploding_Kitten));
        for (int i = 0; i < 55; i++) {
            cards.add(new Card(CardType.Defuse));
        }
    }

    public int getDeckSize() {
        return cards.size();
    }

    Card getCardAt(int index) {
        return cards.get(index);
    }
}
