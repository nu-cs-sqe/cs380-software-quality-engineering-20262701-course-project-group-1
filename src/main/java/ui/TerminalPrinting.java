package ui;

import domain.Card;
import domain.Player;
import java.io.PrintStream;
import java.util.List;

public class TerminalPrinting {

    private final PrintStream out;

    public TerminalPrinting(PrintStream out) {
        this.out = out;
    }

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

    public void printTurn(Player current, int turnsRemaining, int drawPileSize) {
        out.println("Turn: " + current.getName());
        out.println("Turns remaining: " + turnsRemaining);
        out.println("Draw pile size: " + drawPileSize);
    }

    public void printHand(List<Card> hand) {
        if (hand.isEmpty()) {
            out.println("Hand empty");
            return;
        }
        for (int i = 0; i < hand.size(); i++) {
            out.println(i + ": " + hand.get(i).getTypeofCard());
        }
    }

    public void printDiscardTop(List<Card> discardPile) {
        if (discardPile.isEmpty()) {
            out.println("Discard: empty");
            return;
        }
        Card top = discardPile.get(discardPile.size() - 1);
        out.println("Discard top: " + top.getTypeofCard());
    }

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

    public void printEliminated(Player player) {
        out.println(player.getName() + " eliminated");
    }

    public void printWinner(Player winner) {
        out.println(winner.getName() + " won");
    }

    public void printMessage(String message) {
        out.println(message);
    }
}
