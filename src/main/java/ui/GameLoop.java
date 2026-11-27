package ui;

import domain.Card;
import domain.CardType;
import domain.GameManager;
import domain.Player;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Drives a hotseat terminal game using {@link GameManager} and {@link TerminalPrinting}.
 */
public class GameLoop {

    private static final int MIN_PLAYERS = 2;
    private static final int MAX_PLAYERS = 4;

    private final GameManager gameManager;
    private final TerminalPrinting printer;
    private final Scanner in;

    /**
     * Creates a game loop.
     *
     * @param gameManager the game rules and state
     * @param printer terminal output
     * @param in player input
     */
    public GameLoop(GameManager gameManager, TerminalPrinting printer, Scanner in) {
        this.gameManager = gameManager;
        this.printer = printer;
        this.in = in;
    }

    /**
     * Collects player names, then runs turns until the game ends or the user quits.
     */
    public void run() {
        if (!startFromInput()) {
            return;
        }
        runTurns();
    }

    void runTurns() {
        while (!gameManager.isGameOver()) {
            if (!playTurn()) {
                return;
            }
        }
        printer.printWinner(gameManager.getWinner());
    }

    boolean startFromInput() {
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

    private boolean playTurn() {
        Player current = gameManager.getCurrentPlayer();
        printer.printTurn(
                current, gameManager.getTurnsRemaining(), gameManager.getDrawPile().size());
        printer.printDiscardTop(gameManager.getDiscardPile());
        List<Card> hand = current.getHand();
        printer.printHand(hand);
        printer.printMessage("Enter card index, d to draw, or q to quit:");
        if (!in.hasNextLine()) {
            return false;
        }
        String line = in.nextLine().trim();
        if (line.equalsIgnoreCase("q")) {
            return false;
        }
        if (line.equalsIgnoreCase("d")) {
            String seatName = current.getName();
            drawForCurrentPlayer();
            pauseForHotseat(seatName);
            return true;
        }
        int index;
        try {
            index = Integer.parseInt(line);
        } catch (NumberFormatException ex) {
            printer.printMessage("Invalid input");
            return true;
        }
        if (index < 0 || index >= hand.size()) {
            printer.printMessage("Invalid card index");
            return true;
        }
        String seatName = current.getName();
        CardType type = hand.get(index).getTypeofCard();
        if (isNopeable(type)) {
            offerNopes();
        }
        playHandIndex(type);
        pauseForHotseat(seatName);
        return true;
    }

    private void pauseForHotseat(String previousName) {
        if (gameManager.isGameOver()) {
            return;
        }
        Player current = gameManager.getCurrentPlayer();
        if (current.getName().equals(previousName)) {
            return;
        }
        printer.printMessage("Pass device to " + current.getName() + ". Press Enter:");
        if (in.hasNextLine()) {
            in.nextLine();
        }
    }

    private static boolean isNopeable(CardType type) {
        return type == CardType.Attack
                || type == CardType.Shuffle
                || type == CardType.See_The_Future
                || type == CardType.Gamble_Cat;
    }

    private void offerNopes() {
        while (someoneHoldsNope()) {
            printer.printMessage(
                    "Nope? Enter a player name to play Nope, or press Enter to skip:");
            if (!in.hasNextLine()) {
                return;
            }
            String name = in.nextLine().trim();
            if (name.isEmpty()) {
                return;
            }
            playNopeFor(name);
        }
    }

    private boolean someoneHoldsNope() {
        for (Player player : gameManager.getPlayers()) {
            if (player.isAlive() && player.hasCard(CardType.Nope)) {
                return true;
            }
        }
        return false;
    }

    private void playNopeFor(String name) {
        for (Player player : gameManager.getPlayers()) {
            if (player.getName().equals(name)) {
                gameManager.playNope(player);
                printer.printMessage(name + " played Nope");
                return;
            }
        }
    }

    private void playHandIndex(CardType type) {
        boolean noped = gameManager.isNoped();
        try {
            switch (type) {
                case Attack:
                    gameManager.playAttack();
                    break;
                case Shuffle:
                    gameManager.playShuffle();
                    break;
                case See_The_Future:
                    List<Card> seen = gameManager.playSeeTheFuture();
                    printer.printSeeTheFuture(seen);
                    break;
                case Gamble_Cat:
                    printer.printGambleResult(gameManager.playGambleCat());
                    break;
                case Special_Cat_1:
                case Special_Cat_2:
                case Special_Cat_3:
                case Special_Cat_4:
                    printer.printEliminated(gameManager.playSpecialCatSet());
                    break;
                default:
                    printer.printMessage("Cannot play that card now");
                    break;
            }
            if (noped) {
                printer.printMessage(type + " was Noped");
            }
        } catch (IllegalStateException ex) {
            printer.printMessage(ex.getMessage());
        }
    }

    private void drawForCurrentPlayer() {
        Player beforeDraw = gameManager.getCurrentPlayer();
        Card drawn = gameManager.drawCard();
        printer.printMessage("Drew: " + drawn.getTypeofCard());
        if (drawn.getTypeofCard() != CardType.Exploding_Kitten) {
            return;
        }
        if (!beforeDraw.isAlive()) {
            printer.printEliminated(beforeDraw);
            return;
        }
        resolveDefuse();
    }

    private void resolveDefuse() {
        while (true) {
            int maxIndex = gameManager.getDrawPile().size();
            printer.printMessage(
                    "Exploding Kitten! Enter reinsert index (0=top .. " + maxIndex + "=bottom):");
            if (!in.hasNextLine()) {
                return;
            }
            String line = in.nextLine().trim();
            int index;
            try {
                index = Integer.parseInt(line);
            } catch (NumberFormatException ex) {
                printer.printMessage("Invalid input");
                continue;
            }
            try {
                gameManager.playDefuse(index);
                return;
            } catch (IllegalArgumentException | IllegalStateException ex) {
                printer.printMessage(ex.getMessage());
            }
        }
    }
}
