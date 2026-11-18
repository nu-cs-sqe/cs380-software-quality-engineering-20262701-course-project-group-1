package ui;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
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

        runTurnsWithInput(manager, out, attackIndex + "\nq\n");

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

        runTurnsWithInput(manager, out, "d\nq\n");

        assertEquals("B", manager.getCurrentPlayer().getName());
        assertTrue(outputOf(out).contains("Drew: See_The_Future"));
    }
}
