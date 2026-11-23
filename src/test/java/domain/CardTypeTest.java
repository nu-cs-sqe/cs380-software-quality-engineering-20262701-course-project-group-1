package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class CardTypeTest {

    @ParameterizedTest
    @CsvSource({
            "Special_Cat_1, Fire Cat",
            "Special_Cat_2, Water Cat",
            "Special_Cat_3, Earth Cat",
            "Special_Cat_4, Air Cat"
    })
    void toString_specialCat_isDisplayName(CardType type, String expected) {
        assertEquals(expected, type.toString());
    }
}