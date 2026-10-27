package domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.easymock.EasyMock;

import static org.easymock.EasyMock.*;
import static org.junit.jupiter.api.Assertions.*;

public class DeckTests {

    // helpers for Testing
    private List<Card> defuseCards(int count) {
        List<Card> cards = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            cards.add(new Card(CardType.Defuse));
        }
        return cards;
    }

    private void assertTopCards(List<Card> original, List<Card> peeked, int count) {
        assertEquals(count, peeked.size());
        for (int i = 0; i < count; i++) {
            assertSame(original.get(i), peeked.get(i));
        }
    }

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

    @Test
    public void shuffleDeck_oneCard_sameCardRemains() {
        Card defuse = new Card(CardType.Defuse);
        Deck deck = new Deck(List.of(defuse));

        deck.shuffleDeck();

        assertEquals(1, deck.getDeckSize());
        assertSame(defuse, deck.getCardAt(0));
    }

    @Test
    public void shuffleDeck_twoCards_reordersUsingRandom() {
        Card defuse = new Card(CardType.Defuse);
        Card kitten = new Card(CardType.Exploding_Kitten);

        Random random = createMock(Random.class);
        expect(random.nextInt(2)).andReturn(0);
        replay(random);

        Deck deck = new Deck(List.of(defuse, kitten), random);
        deck.shuffleDeck();

        assertEquals(2, deck.getDeckSize());
        assertSame(kitten, deck.getCardAt(0));
        assertSame(defuse, deck.getCardAt(1));
        verify(random);
    }


    @Test
    public void shuffleDeck_fullDeck_sameCardsNewOrder() {
        List<Card> original = new ArrayList<>();
        original.add(new Card(CardType.Exploding_Kitten));
        for (int i = 0; i < 55; i++) {
            original.add(new Card(CardType.Defuse));
        }

        Random random = createMock(Random.class);
        for (int i = 56; i > 1; i--) {
            expect(random.nextInt(i)).andReturn(0);
        }
        replay(random);

        Deck deck = new Deck(original, random);
        deck.shuffleDeck();

        // same size
        assertEquals(56, deck.getDeckSize());

        // new order: rotated by one, top card moved to the bottom
        for (int i = 0; i < 55; i++) {
            assertSame(original.get(i + 1), deck.getCardAt(i));
        }
        assertSame(original.get(0), deck.getCardAt(55));

        // duplicates still present
        int defuseCount = 0;
        for (int i = 0; i < deck.getDeckSize(); i++) {
            if (deck.getCardAt(i).getTypeofCard() == CardType.Defuse) {
                defuseCount++;
            }
        }
        assertEquals(55, defuseCount);

        verify(random);
    }

    @Test
    public void peekTopofDeck_emptyDeckPeekZero_throwsIllegalStateException() {
        Deck deck = new Deck(new ArrayList<>());
        assertThrows(IllegalStateException.class, () -> deck.peekTopofDeck(0));
    }

    @Test
    public void peekTopofDeck_negativePeek_throwsIllegalArgumentException() {
        Deck deck = new Deck(new ArrayList<>());
        assertThrows(IllegalArgumentException.class, () -> deck.peekTopofDeck(-1));
    }

    @Test
    public void peekTopofDeck_oneCardPeekZero_returnsEmptyList() {
        Deck deck = new Deck(defuseCards(1));

        List<Card> peeked = deck.peekTopofDeck(0);

        assertTrue(peeked.isEmpty());
        assertEquals(1, deck.getDeckSize());
    }

    @Test
    public void peekTopofDeck_twoCardsPeekOne_returnsTopCard(){
        List<Card> cards = defuseCards(2);
        Deck deck = new Deck(cards);

        List<Card> peeked = deck.peekTopofDeck(1);

        assertTopCards(cards, peeked, 1);
        assertEquals(2, deck.getDeckSize());
    }

    @Test
    public void peekTopofDeck_fullDeckPeekThree_returnsTopThree() {
        List<Card> cards = defuseCards(56);
        Deck deck = new Deck(cards);

        List<Card> peeked = deck.peekTopofDeck(3);

        assertTopCards(cards, peeked, 3);
        assertEquals(56, deck.getDeckSize());
    }

    @Test
    public void peekTopofDeck_fullDeckPeekAllButOne_returnsTop55() {
        List<Card> cards = defuseCards(56);
        Deck deck = new Deck(cards);

        List<Card> peeked = deck.peekTopofDeck(55);

        assertTopCards(cards, peeked, 55);
        assertEquals(56, deck.getDeckSize());
    }

    @Test
    public void peekTopofDeck_fullDeckPeekAll_returnsWholeDeck() {
        List<Card> cards = defuseCards(56);
        Deck deck = new Deck(cards);

        List<Card> peeked = deck.peekTopofDeck(56);

        assertTopCards(cards, peeked, 56);
        assertEquals(56, deck.getDeckSize());
    }

    @Test
    public void peekTopofDeck_peekMoreThanDeckSize_throwsIllegalStateException() {
        Deck deck = new Deck(defuseCards(56));
        assertThrows(IllegalStateException.class, () -> deck.peekTopofDeck(57));
    }

    @Test
    public void insertCardAt_indexZero_cardIsOnTop() {
        Deck deck = new Deck(defuseCards(56), new Random());
        Card kitten = new Card(CardType.Exploding_Kitten);

        deck.insertCardAt(kitten, 0);

        assertEquals(57, deck.getDeckSize());
        assertSame(kitten, deck.getCardAt(0));
    }

    @Test
    public void insertCardAt_indexOne_cardIsSecond() {
        List<Card> original = defuseCards(56);
        Deck deck = new Deck(original, new Random());
        Card kitten = new Card(CardType.Exploding_Kitten);

        deck.insertCardAt(kitten, 1);

        assertEquals(57, deck.getDeckSize());
        assertSame(original.get(0), deck.getCardAt(0));
        assertSame(kitten, deck.getCardAt(1));
        assertSame(original.get(1), deck.getCardAt(2));
    }

    @Test
    public void insertCardAt_index56_cardIsOnBottom() {
        Deck deck = new Deck(defuseCards(56), new Random());
        Card kitten = new Card(CardType.Exploding_Kitten);

        deck.insertCardAt(kitten, 56);

        assertEquals(57, deck.getDeckSize());
        assertSame(kitten, deck.getCardAt(56));
    }

    @Test
    public void insertCardAt_negativeIndex_throwsAndDeckUnchanged() {
        Deck deck = new Deck(defuseCards(56), new Random());

        assertThrows(IllegalArgumentException.class,
                () -> deck.insertCardAt(new Card(CardType.Exploding_Kitten), -1));
        assertEquals(56, deck.getDeckSize());
    }

    @Test
    public void insertCardAt_nullCard_throwsAndDeckUnchanged() {
        Deck deck = new Deck(defuseCards(56), new Random());

        assertThrows(IllegalArgumentException.class,
                () -> deck.insertCardAt(null, 1));
        assertEquals(56, deck.getDeckSize());
    }

    @Test
    public void insertCardAt_indexPastBottom_throwsAndDeckUnchanged() {
        Deck deck = new Deck(defuseCards(56), new Random());

        assertThrows(IllegalArgumentException.class,
                () -> deck.insertCardAt(new Card(CardType.Exploding_Kitten), 57));
        assertEquals(56, deck.getDeckSize());
    }

    @Test
    public void removeAllCardsofType_emptyDeck_returnsEmptyList() {
        Deck deck = new Deck(new ArrayList<>(), new Random());

        List<Card> removed = deck.removeAllCardsofType(CardType.Defuse);

        assertTrue(removed.isEmpty());
        assertEquals(0, deck.getDeckSize());
    }

    @Test
    public void removeAllCardsofType_noMatches_returnsEmptyListDeckUnchanged() {
        List<Card> original = List.of(
                new Card(CardType.Exploding_Kitten),
                new Card(CardType.Exploding_Kitten),
                new Card(CardType.Exploding_Kitten));
        Deck deck = new Deck(original, new Random());

        List<Card> removed = deck.removeAllCardsofType(CardType.Defuse);

        assertTrue(removed.isEmpty());
        assertEquals(3, deck.getDeckSize());
        for (int i = 0; i < 3; i++) {
            assertSame(original.get(i), deck.getCardAt(i));
        }
    }

    @Test
    public void removeAllCardsofType_oneMatch_returnsItAndRemovesFromDeck() {
        Card kitten1 = new Card(CardType.Exploding_Kitten);
        Card defuse = new Card(CardType.Defuse);
        Card kitten2 = new Card(CardType.Exploding_Kitten);
        Deck deck = new Deck(List.of(kitten1, defuse, kitten2), new Random());

        List<Card> removed = deck.removeAllCardsofType(CardType.Defuse);

        assertEquals(1, removed.size());
        assertSame(defuse, removed.get(0));
        assertEquals(2, deck.getDeckSize());
        assertSame(kitten1, deck.getCardAt(0));
        assertSame(kitten2, deck.getCardAt(1));
    }

    @Test
    public void removeAllCardsofType_nullType_throwsAndDeckUnchanged() {
        Deck deck = new Deck(defuseCards(3), new Random());

        assertThrows(IllegalArgumentException.class,
                () -> deck.removeAllCardsofType(null));
        assertEquals(3, deck.getDeckSize());
    }
}
