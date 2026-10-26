package domain;

import java.util.ArrayList;
import java.util.List;

public class Deck {
    private final List<Card> cards = new ArrayList<>();

    public Deck() {
        for (int i = 0; i < 56; i++) {
            cards.add(new Card(CardType.Defuse));
        }
    }

    public int getDeckSize() {
        return cards.size();
    }
}
