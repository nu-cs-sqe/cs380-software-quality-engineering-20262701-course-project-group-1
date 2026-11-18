package ui;

import domain.GameManager;
import domain.Player;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GameLoop {

    private static final int MIN_PLAYERS = 2;
    private static final int MAX_PLAYERS = 4;

    private final GameManager gameManager;
    private final TerminalPrinting printer;
    private final Scanner in;

    public GameLoop(GameManager gameManager, TerminalPrinting printer, Scanner in) {
        this.gameManager = gameManager;
        this.printer = printer;
        this.in = in;
    }

    public void run() {
        if (!startFromInput()) {
            return;
        }
    }

    private boolean startFromInput() {
        printer.printMessage("Enter number of players (2-4):");
        if (!in.hasNextInt()) {
            printer.printMessage("Invalid player count");
            return false;
        }
        int count = in.nextInt();
        in.nextLine();
        if (count < MIN_PLAYERS || count > MAX_PLAYERS) {
            printer.printMessage("Invalid player count");
            return false;
        }
        List<String> names = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            printer.printMessage("Enter name for player " + i + ":");
            if (!in.hasNextLine()) {
                printer.printMessage("Missing player name");
                return false;
            }
            names.add(in.nextLine());
        }
        try {
            gameManager.startNewGame(names);
        } catch (IllegalArgumentException ex) {
            printer.printMessage(ex.getMessage());
            return false;
        }
        Player current = gameManager.getCurrentPlayer();
        printer.printSetup(gameManager.getPlayers(), current);
        return true;
    }
}
