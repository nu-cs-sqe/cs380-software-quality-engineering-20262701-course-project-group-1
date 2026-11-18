package ui;

import domain.GameManager;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Entry point for the game.
 */
public class Main {

    /**
     * Starts game on stdin/stdout.
     *
     * @param args unused
     */
    public static void main(String[] args) {
        GameManager gameManager = new GameManager();
        TerminalPrinting printer = new TerminalPrinting(System.out);
        Scanner in = new Scanner(System.in, StandardCharsets.UTF_8.name());
        new GameLoop(gameManager, printer, in).run();
    }
}
