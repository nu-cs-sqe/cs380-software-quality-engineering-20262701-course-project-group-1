package domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeckTests {
    @Test
    public void constructor_createsDeckOf56Cards() {
        Deck deck = new Deck();
        assertEquals(56, deck.getDeckSize());
    }
}
