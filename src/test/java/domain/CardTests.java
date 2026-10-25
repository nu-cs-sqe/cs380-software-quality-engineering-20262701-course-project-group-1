package domain;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;


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

    @ParameterizedTest
    @EnumSource(CardType.class)
    public void getTypeofCard_ReturnsCorrectType_GivenatConstruction(CardType type) {
        Card card = new Card(type);
        assertEquals(type, card.getTypeofCard());
    }
}
