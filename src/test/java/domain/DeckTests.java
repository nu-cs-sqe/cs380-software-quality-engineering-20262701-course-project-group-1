package domain;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    public void drawCard_oneCardDeck_returnsDefuseAndDeckIsEmpty() {
        Card defuse = new Card(CardType.Defuse);
        Deck deck = new Deck(List.of(defuse));

        Card drawn = deck.drawCard();

        assertSame(defuse, drawn);
        assertEquals(0, deck.getDeckSize());
    }

    @Test
    public void drawCard_twoCardDeck_returnsTopCardAndLeavesRemaining() {
        Card defuse = new Card(CardType.Defuse);
        Card kitten = new Card(CardType.Exploding_Kitten);
        Deck deck = new Deck(List.of(defuse, kitten));

        Card drawn = deck.drawCard();

        assertSame(defuse, drawn);
        assertEquals(1, deck.getDeckSize());
        assertSame(kitten, deck.getCardAt(0));
    }
}
