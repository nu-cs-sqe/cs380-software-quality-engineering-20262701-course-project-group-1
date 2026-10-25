package domain;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
public class CardTests {
    @Test
    public void CardConstructor_withDefuse_ConstructsCorrectlyStoresDefuseType() {
        Card card = new Card(CardType.Defuse);
        assertEquals(CardType.Defuse, card.getTypeofCard());
    }
}
