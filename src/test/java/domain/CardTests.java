package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class CardTests {
    @Test
    public void cardConstructor_withDefuse_storesDefuseType() {
        Card card = new Card(CardType.Defuse);
        assertEquals(CardType.Defuse, card.getTypeofCard());
    }

    @Test
    public void cardConstructor_withNull_throwsIllegalArgumentException() {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> new Card(null)
        );
        assertEquals("Not a valid Card Type, Card Type cannot be null", e.getMessage());
    }

    @ParameterizedTest
    @EnumSource(CardType.class)
    public void getTypeofCard_returnsTypeGivenAtConstruction(CardType type) {
        Card card = new Card(type);
        assertEquals(type, card.getTypeofCard());
    }
}
