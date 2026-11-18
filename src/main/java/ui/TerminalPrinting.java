package ui;

import domain.Card;
import domain.Player;
import java.io.PrintStream;
import java.util.List;

/**
 * Terminal output for Exploding Kittens session.
 */
public class TerminalPrinting {

    private final PrintStream out;

    /**
     * Creates a printer that writes to the given stream.
     *
     * @param out where to print
     */
    public TerminalPrinting(PrintStream out) {
        this.out = out;
    }

    /**
     * Prints each player's alive status and whose turn it is.
     *
     * @param players the seated players
     * @param currentPlayer the player whose turn it is
     */
    public void printSetup(List<Player> players, Player currentPlayer) {
        for (Player player : players) {
            if (player.isAlive()) {
                out.println(player.getName() + " alive");
            } else {
                out.println(player.getName() + " eliminated");
            }
        }
        out.println("Current turn: " + currentPlayer.getName());
    }

    /**
     * Prints the current turn banner.
     *
     * @param current the current player
     * @param turnsRemaining how many turns that player still owes
     * @param drawPileSize how many cards remain in the draw pile
     */
    public void printTurn(Player current, int turnsRemaining, int drawPileSize) {
        out.println("Turn: " + current.getName());
        out.println("Turns remaining: " + turnsRemaining);
        out.println("Draw pile size: " + drawPileSize);
    }

    /**
     * Prints a numbered hand, or an empty-hand line.
     *
     * @param hand the cards to show
     */
    public void printHand(List<Card> hand) {
        if (hand.isEmpty()) {
            out.println("Hand empty");
            return;
        }
        for (int i = 0; i < hand.size(); i++) {
            out.println(i + ": " + hand.get(i).getTypeofCard());
        }
    }

    /**
     * Prints the top of the discard pile, or that it is empty.
     *
     * @param discardPile the discard pile, top last
     */
    public void printDiscardTop(List<Card> discardPile) {
        if (discardPile.isEmpty()) {
            out.println("Discard: empty");
            return;
        }
        Card top = discardPile.get(discardPile.size() - 1);
        out.println("Discard top: " + top.getTypeofCard());
    }

    /**
     * Prints the cards revealed by See the Future.
     *
     * @param topCards top of the draw pile first
     */
    public void printSeeTheFuture(List<Card> topCards) {
        out.println("See the Future:");
        if (topCards.isEmpty()) {
            out.println("No cards");
            return;
        }
        for (int i = 0; i < topCards.size(); i++) {
            out.println((i + 1) + ": " + topCards.get(i).getTypeofCard());
        }
    }

    /**
     * Prints that a player was eliminated.
     *
     * @param player the eliminated player
     */
    public void printEliminated(Player player) {
        out.println(player.getName() + " eliminated");
    }

    /**
     * Prints the winner.
     *
     * @param winner the winning player
     */
    public void printWinner(Player winner) {
        out.println(winner.getName() + " won");
    }

    /**
     * Prints a single message line.
     *
     * @param message the text to print
     */
    public void printMessage(String message) {
        out.println(message);
    }
}
