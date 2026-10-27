package domain;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DeckTests {
    @Test
    public void constructor_createsDeckOf56Cards() {
        Deck deck = new Deck();
        assertEquals(56, deck.getDeckSize());
    }

    @Test
    public void constructor_firstCardIsExplodingKitten() {
        Deck deck = new Deck();
        assertEquals(56, deck.getDeckSize());
        assertEquals(CardType.Exploding_Kitten, deck.getCardAt(0).getTypeofCard());
    }

    @Test
    public void constructor_lastCardIsDefuse() {
        Deck deck = new Deck();
        assertEquals(56, deck.getDeckSize());
        assertEquals(CardType.Defuse, deck.getCardAt(55).getTypeofCard());
    }

    @Test
    public void drawCard_emptyDeck_throwsIllegalStateException() {
        Deck deck = new Deck(new ArrayList<>());
        assertThrows(IllegalStateException.class, deck::drawCard);
    }
}
