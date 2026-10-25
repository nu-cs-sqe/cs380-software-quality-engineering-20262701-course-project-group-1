package domain;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CardTests {
    @Test
    public void CardConstructor_withDefuse_ConstructsCorrectlyStoresDefuseType() {
        Card card = new Card(CardType.Defuse);
        assertEquals(CardType.Defuse, card.getTypeofCard());
    }
    @Test
    public void CardConstructor_withNull_ConstructsIncorrectly_ThrowsException() {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> new Card(null)
        );
        assertEquals("Not a valid Card Type, Card Type cannot be null", e.getMessage());
    }
}
