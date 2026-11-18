package ui;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import domain.Card;
import domain.CardType;
import domain.GameManager;
import domain.GameManagerFixtures;
import domain.Player;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;
import org.junit.jupiter.api.Test;

class GameLoopTest {

    private static GameLoop loopWithInput(GameManager manager, ByteArrayOutputStream out, String input) {
        TerminalPrinting printer = new TerminalPrinting(
                new PrintStream(out, true, StandardCharsets.UTF_8));
        Scanner scanner = new Scanner(
                new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)),
                StandardCharsets.UTF_8.name());
        return new GameLoop(manager, printer, scanner);
    }

    private static String outputOf(ByteArrayOutputStream out) {
        return out.toString(StandardCharsets.UTF_8);
    }

    private static int indexOfType(Player player, CardType type) {
        List<Card> hand = player.getHand();
        for (int i = 0; i < hand.size(); i++) {
            if (hand.get(i).getTypeofCard() == type) {
                return i;
            }
        }
        return -1;
    }

    private static void ensureInHand(Player player, CardType type) {
        if (indexOfType(player, type) < 0) {
            player.addCard(new Card(type));
        }
    }

    private static void runTurnsWithInput(
            GameManager manager, ByteArrayOutputStream out, String turnInput) {
        GameLoop loop = loopWithInput(manager, out, turnInput);
        loop.runTurns();
    }

    @Test
    void constructor_doesNotThrow() {
        GameManager manager = new GameManager();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        assertDoesNotThrow(() -> loopWithInput(manager, out, ""));
    }

    @Test
    void run_onePlayer_rejectsAndDoesNotStart() {
        GameManager manager = new GameManager();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GameLoop loop = loopWithInput(manager, out, "1\n");

        loop.run();

        assertTrue(manager.getPlayers().isEmpty());
        assertTrue(outputOf(out).length() > 0);
    }

    @Test
    void run_fourPlayers_startsAndPrintsSetup() {
        GameManager manager = new GameManager();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GameLoop loop = loopWithInput(manager, out, "4\nA\nB\nC\nD\nq\n");

        loop.run();

        assertEquals(4, manager.getPlayers().size());
        assertEquals("A", manager.getPlayers().get(0).getName());
        assertEquals("D", manager.getPlayers().get(3).getName());
        String printed = outputOf(out);
        assertTrue(printed.contains("A alive"));
        assertTrue(printed.contains("B alive"));
        assertTrue(printed.contains("C alive"));
        assertTrue(printed.contains("D alive"));
        assertTrue(printed.contains("Current turn:"));
    }

    @Test
    void run_blankName_rejectsAndDoesNotStart() {
        GameManager manager = new GameManager();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GameLoop loop = loopWithInput(manager, out, "2\n\nB\n");

        loop.run();

        assertTrue(manager.getPlayers().isEmpty());
        assertTrue(outputOf(out).length() > 0);
    }

    @Test
    void run_playAttack_byIndex_movesTurn() {
        GameManager manager = GameManagerFixtures.alwaysFirstPlayer();
        manager.startNewGame(List.of("A", "B"));
        Player a = manager.getCurrentPlayer();
        ensureInHand(a, CardType.Attack);
        int attackIndex = indexOfType(a, CardType.Attack);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        runTurnsWithInput(manager, out, attackIndex + "\n\nq\n");

        assertEquals("B", manager.getCurrentPlayer().getName());
        assertEquals(2, manager.getTurnsRemaining());
        assertTrue(outputOf(out).contains("Turn:"));
    }

    @Test
    void run_playShuffle_byIndex_keepsTurn() {
        GameManager manager = GameManagerFixtures.alwaysFirstPlayer();
        manager.startNewGame(List.of("A", "B"));
        Player a = manager.getCurrentPlayer();
        ensureInHand(a, CardType.Shuffle);
        int shuffleIndex = indexOfType(a, CardType.Shuffle);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        runTurnsWithInput(manager, out, shuffleIndex + "\nq\n");

        assertEquals("A", manager.getCurrentPlayer().getName());
        assertEquals(1, manager.getTurnsRemaining());
    }

    @Test
    void run_playSeeTheFuture_byIndex_printsPeek() {
        GameManager manager = GameManagerFixtures.alwaysFirstPlayer();
        manager.startNewGame(List.of("A", "B"));
        Player a = manager.getCurrentPlayer();
        ensureInHand(a, CardType.See_The_Future);
        int index = indexOfType(a, CardType.See_The_Future);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        runTurnsWithInput(manager, out, index + "\nq\n");

        assertEquals("A", manager.getCurrentPlayer().getName());
        assertTrue(outputOf(out).contains("See the Future:"));
    }

    @Test
    void run_draw_nonExploding_endsTurn() {
        GameManager manager = GameManagerFixtures.alwaysFirstPlayer();
        manager.startNewGame(List.of("A", "B"));
        GameManagerFixtures.putOnTop(manager, new Card(CardType.See_The_Future));
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        runTurnsWithInput(manager, out, "d\n\nq\n");

        assertEquals("B", manager.getCurrentPlayer().getName());
        assertTrue(outputOf(out).contains("Drew: See_The_Future"));
    }

    @Test
    void run_drawExplodingKitten_withDefuse_reinsertsAtTop() {
        GameManager manager = GameManagerFixtures.alwaysFirstPlayer();
        manager.startNewGame(List.of("A", "B"));
        Player a = manager.getCurrentPlayer();
        Card kitten = new Card(CardType.Exploding_Kitten);
        GameManagerFixtures.putOnTop(manager, kitten);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        runTurnsWithInput(manager, out, "d\n0\n\nq\n");

        assertSame(kitten, manager.getDrawPile().get(0));
        assertEquals("B", manager.getCurrentPlayer().getName());
        assertEquals(0, countType(a.getHand(), CardType.Exploding_Kitten));
    }

    @Test
    void run_drawExplodingKitten_withDefuse_reinsertsAtBottom() {
        GameManager manager = GameManagerFixtures.alwaysFirstPlayer();
        manager.startNewGame(List.of("A", "B"));
        Card kitten = new Card(CardType.Exploding_Kitten);
        GameManagerFixtures.putOnTop(manager, kitten);
        int bottomAfterDraw = manager.getDrawPile().size() - 1;
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        runTurnsWithInput(manager, out, "d\n" + bottomAfterDraw + "\n\nq\n");

        List<Card> pile = manager.getDrawPile();
        assertSame(kitten, pile.get(pile.size() - 1));
        assertEquals("B", manager.getCurrentPlayer().getName());
    }

    @Test
    void run_drawExplodingKitten_withoutDefuse_eliminatesAndEndsGame() {
        GameManager manager = GameManagerFixtures.alwaysFirstPlayer();
        manager.startNewGame(List.of("A", "B"));
        Player a = manager.getCurrentPlayer();
        a.removeCardOfType(CardType.Defuse);
        GameManagerFixtures.putOnTop(manager, new Card(CardType.Exploding_Kitten));
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        runTurnsWithInput(manager, out, "d\n");

        assertFalse(a.isAlive());
        assertTrue(manager.isGameOver());
        assertEquals("B", manager.getWinner().getName());
        String printed = outputOf(out);
        assertTrue(printed.contains("A"));
        assertTrue(printed.toLowerCase().contains("eliminated"));
        assertTrue(printed.contains("B"));
        assertTrue(printed.toLowerCase().contains("won"));
    }

    @Test
    void run_playDefuseFromMenu_printsErrorAndHandUnchanged() {
        GameManager manager = GameManagerFixtures.alwaysFirstPlayer();
        manager.startNewGame(List.of("A", "B"));
        Player a = manager.getCurrentPlayer();
        ensureInHand(a, CardType.Defuse);
        int defuseIndex = indexOfType(a, CardType.Defuse);
        List<Card> handBefore = a.getHand();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        runTurnsWithInput(manager, out, defuseIndex + "\nq\n");

        assertEquals(handBefore, a.getHand());
        assertEquals("A", manager.getCurrentPlayer().getName());
        assertTrue(outputOf(out).contains("Cannot play that card now"));
    }

    @Test
    void run_invalidIndex_printsErrorAndTurnUnchanged() {
        GameManager manager = GameManagerFixtures.alwaysFirstPlayer();
        manager.startNewGame(List.of("A", "B"));
        Player a = manager.getCurrentPlayer();
        List<Card> handBefore = a.getHand();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        runTurnsWithInput(manager, out, "99\nq\n");

        assertEquals(handBefore, a.getHand());
        assertEquals("A", manager.getCurrentPlayer().getName());
        assertTrue(outputOf(out).contains("Invalid card index"));
    }

    @Test
    void run_nonIntegerInput_printsErrorAndTurnUnchanged() {
        GameManager manager = GameManagerFixtures.alwaysFirstPlayer();
        manager.startNewGame(List.of("A", "B"));
        assertEquals("A", manager.getCurrentPlayer().getName());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        runTurnsWithInput(manager, out, "x\nq\n");

        assertEquals("A", manager.getCurrentPlayer().getName());
        assertTrue(outputOf(out).contains("Invalid input"));
    }

    @Test
    void run_afterTurnEnds_promptsHotseatPass() {
        GameManager manager = GameManagerFixtures.alwaysFirstPlayer();
        manager.startNewGame(List.of("A", "B"));
        GameManagerFixtures.putOnTop(manager, new Card(CardType.See_The_Future));
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        runTurnsWithInput(manager, out, "d\n\nq\n");

        assertEquals("B", manager.getCurrentPlayer().getName());
        assertTrue(outputOf(out).contains("Pass device to B"));
        assertTrue(outputOf(out).contains("Press Enter"));
    }

    private static int countType(List<Card> cards, CardType type) {
        int count = 0;
        for (Card card : cards) {
            if (card.getTypeofCard() == type) {
                count++;
            }
        }
        return count;
    }
}
