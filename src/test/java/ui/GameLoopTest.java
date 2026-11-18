package ui;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import domain.GameManager;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
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
        GameLoop loop = loopWithInput(manager, out, "4\nA\nB\nC\nD\n");

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
}
