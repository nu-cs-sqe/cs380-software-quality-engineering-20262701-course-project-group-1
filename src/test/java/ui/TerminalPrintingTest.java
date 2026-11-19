package ui;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import domain.Card;
import domain.CardType;
import domain.Player;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TerminalPrintingTest {

    private ByteArrayOutputStream buffer;
    private TerminalPrinting printer;

    @BeforeEach
    void setUp() {
        buffer = new ByteArrayOutputStream();
        printer = new TerminalPrinting(new PrintStream(buffer, true, StandardCharsets.UTF_8));
    }

    private String output() {
        return buffer.toString(StandardCharsets.UTF_8);
    }

    @Test
    void constructor_doesNotThrow() {
        assertDoesNotThrow(() -> new TerminalPrinting(System.out));
    }

    @Test
    void printSetup_twoPlayers_currentA() {
        Player a = new Player("A");
        Player b = new Player("B");

        printer.printSetup(List.of(a, b), a);

        assertEquals("A alive\nB alive\nCurrent turn: A\n", output());
    }

    @Test
    void printSetup_fourPlayers_currentD() {
        Player a = new Player("A");
        Player b = new Player("B");
        Player c = new Player("C");
        Player d = new Player("D");

        printer.printSetup(List.of(a, b, c, d), d);

        assertEquals(
                "A alive\nB alive\nC alive\nD alive\nCurrent turn: D\n",
                output());
    }

    @Test
    void printTurn_playerA_oneTurn_fiveInPile() {
        printer.printTurn(new Player("A"), 1, 5);

        assertEquals("Turn: A\nTurns remaining: 1\nDraw pile size: 5\n", output());
    }

    @Test
    void printTurn_playerB_twoTurns_emptyPile() {
        printer.printTurn(new Player("B"), 2, 0);

        assertEquals("Turn: B\nTurns remaining: 2\nDraw pile size: 0\n", output());
    }

    @Test
    void printHand_twoCards() {
        printer.printHand(List.of(new Card(CardType.Defuse), new Card(CardType.Attack)));

        assertEquals("0: Defuse\n1: Attack\n", output());
    }

    @Test
    void printHand_empty() {
        printer.printHand(List.of());

        assertEquals("Hand empty\n", output());
    }

    @Test
    void printHand_threeCards() {
        printer.printHand(List.of(
                new Card(CardType.See_The_Future),
                new Card(CardType.Shuffle),
                new Card(CardType.Defuse)));

        assertEquals("0: See_The_Future\n1: Shuffle\n2: Defuse\n", output());
    }

    @Test
    void printDiscardTop_empty() {
        printer.printDiscardTop(List.of());

        assertEquals("Discard: empty\n", output());
    }

    @Test
    void printDiscardTop_endsWithAttack() {
        printer.printDiscardTop(List.of(new Card(CardType.Defuse), new Card(CardType.Attack)));

        assertEquals("Discard top: Attack\n", output());
    }

    @Test
    void printSeeTheFuture_threeCards() {
        printer.printSeeTheFuture(List.of(
                new Card(CardType.Exploding_Kitten),
                new Card(CardType.Defuse),
                new Card(CardType.Shuffle)));

        assertEquals(
                "See the Future:\n1: Exploding_Kitten\n2: Defuse\n3: Shuffle\n",
                output());
    }

    @Test
    void printSeeTheFuture_empty() {
        printer.printSeeTheFuture(List.of());

        assertEquals("See the Future:\nNo cards\n", output());
    }

    @Test
    void printEliminated_playerA() {
        printer.printEliminated(new Player("A"));

        String text = output();
        assertTrue(text.contains("A"));
        assertTrue(text.toLowerCase().contains("eliminated"));
    }

    @Test
    void printWinner_playerB() {
        printer.printWinner(new Player("B"));

        String text = output();
        assertTrue(text.contains("B"));
        assertTrue(text.toLowerCase().contains("won"));
    }

    @Test
    void printMessage_printsExactLine() {
        printer.printMessage("error");

        assertEquals("error\n", output());
    }

    @Test
    void printGambleResult_lucky_printsLuckyLine() {
        printer.printGambleResult(true);

        String expected = "Gamble Cat: lucky! Your turn ends without drawing"
                + System.lineSeparator();
        assertEquals(expected, buffer.toString(StandardCharsets.UTF_8));
    }
}
