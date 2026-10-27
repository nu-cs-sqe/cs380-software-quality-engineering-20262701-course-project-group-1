package domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

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

    @Test
    public void drawCard_fullDeck_returnsTopCardAndLeavesRestInOrder() {
        Deck deck = new Deck();

        Card expectedTop = deck.getCardAt(0);
        List<Card> expectedRemaining = new ArrayList<>();
        for (int i = 1; i < deck.getDeckSize(); i++) {
            expectedRemaining.add(deck.getCardAt(i));
        }

        Card drawn = deck.drawCard();

        assertSame(expectedTop, drawn);
        assertEquals(CardType.Exploding_Kitten, drawn.getTypeofCard());
        assertEquals(55, deck.getDeckSize());
        for (int i = 0; i < expectedRemaining.size(); i++) {
            assertSame(expectedRemaining.get(i), deck.getCardAt(i));
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 3, 56})
    public void getDeckSize_returnsNumberOfCardsInDeck(int size) {
        List<Card> cards = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            cards.add(new Card(CardType.Defuse));
        }
        Deck deck = new Deck(cards);

        assertEquals(size, deck.getDeckSize());
    }

    @Test
    public void shuffleDeck_emptyDeck_remainsEmpty() {
        Deck deck = new Deck(new ArrayList<>());

        assertDoesNotThrow(deck::shuffleDeck);
        assertEquals(0, deck.getDeckSize());
    }
}
