package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Runs a game: sets up players, hands and the draw pile, and handles playing cards.
 */
public class GameManager {

    private static final String PLAYER_COUNT_OUT_OF_RANGE = "gameManager.playerCount.outOfRange";
    private static final String PLAYER_NAME_BLANK = "gameManager.playerName.blank";
    private static final String NOT_STARTED = "gameManager.notStarted";
    private static final String NOT_IN_HAND = "gameManager.play.notInHand";
    private static final int MIN_PLAYERS = 2;
    private static final int MAX_PLAYERS = 4;
    private static final int TOTAL_DEFUSES = 6;
    private static final int STARTING_GENERIC_CARDS = 5;
    private static final int GENERIC_SUPPLY = 20;

    private final List<Player> players = new ArrayList<>();
    private final List<Card> discardPile = new ArrayList<>();
    private final TurnManager turnManager = new TurnManager();
    private final Random random;
    private Deck deck;

    /**
     * Creates a game manager that uses a new {@link Random} for shuffling and turn order.
     */
    public GameManager() {
        this(new Random());
    }

    GameManager(Random random) {
        this.random = random;
    }

    /**
     * Starts a new game: creates the players, deals starting hands and picks who goes first.
     *
     * @param playerNames the names of the players, 2 to 4 of them, none blank
     * @throws IllegalArgumentException if the number of players is out of range or a name is
     *     blank
     */
    public void startNewGame(List<String> playerNames) {
        if (playerNames.size() < MIN_PLAYERS || playerNames.size() > MAX_PLAYERS) {
            throw new IllegalArgumentException(PLAYER_COUNT_OUT_OF_RANGE);
        }
        for (String name : playerNames) {
            if (name.isBlank()) {
                throw new IllegalArgumentException(PLAYER_NAME_BLANK);
            }
        }
        players.clear();
        discardPile.clear();
        for (String name : playerNames) {
            players.add(new Player(name));
        }
        dealStartingHands();
        turnManager.pickStart(players.size(), random);
    }

    public Player getCurrentPlayer() {
        return players.get(turnManager.getCurrentIndex());
    }

    public List<Player> getPlayers() {
        return new ArrayList<>(players);
    }

    /**
     * Returns a copy of the draw pile, top card first.
     *
     * @return the cards in the draw pile
     */
    public List<Card> getDrawPile() {
        List<Card> pile = new ArrayList<>();
        for (int i = 0; i < deck.getDeckSize(); i++) {
            pile.add(deck.getCardAt(i));
        }
        return pile;
    }

    /**
     * Plays an Attack card from the current player's hand.
     *
     * <p>The card goes to the discard pile and the next player must take two turns, plus any
     * turns the current player still owed from an earlier attack.
     *
     * @throws IllegalStateException if the game has not started or the current player has no
     *     Attack card
     */
    public void playAttack() {
        playFromHand(CardType.Attack);
        turnManager.attackNextPlayer();
    }
    /**
     * Plays a Shuffle card from the current player's hand and shuffles the draw pile.
     *
     * <p>The current player keeps their turn.
     *
     * @throws IllegalStateException if the game has not started or the current player has no
     *     Shuffle card
     */
    public void playShuffle() {
        playFromHand(CardType.Shuffle);
        deck.shuffleDeck();
    }

    public List<Card> getDiscardPile() {
        return new ArrayList<>(discardPile);
    }

    /**
     * Returns how many turns the current player still owes.
     *
     * @return the number of turns remaining
     * @throws IllegalStateException if the game has not started
     */
    public int getTurnsRemaining() {
        requireStarted();
        return turnManager.getTurnsRemaining();
    }
    private void dealStartingHands() {
        deck = new Deck(startingCards(), random);
        List<Card> defuses = deck.removeAllCardsofType(CardType.Defuse);
        deck.shuffleDeck();
        for (Player player : players) {
            for (int i = 0; i < STARTING_GENERIC_CARDS; i++) {
                player.addCard(deck.drawCard());
            }
            player.addCard(defuses.remove(0));
        }
        for (Card defuse : defuses) {
            deck.addCard(defuse);
        }
        int explodingKittens = players.size() - 1;
        for (int i = 0; i < explodingKittens; i++) {
            deck.addCard(new Card(CardType.Exploding_Kitten));
        }
        deck.shuffleDeck();
    }

    private static List<Card> startingCards() {
        List<Card> cards = new ArrayList<>();
        for (int i = 0; i < GENERIC_SUPPLY; i++) {
            cards.add(new Card(CardType.See_The_Future));
        }
        for (int i = 0; i < TOTAL_DEFUSES; i++) {
            cards.add(new Card(CardType.Defuse));
        }
        return cards;
    }

    private void requireStarted() {
        if (players.isEmpty()) {
            throw new IllegalStateException(NOT_STARTED);
        }
    }

    private static int countOf(List<Card> cards, CardType type) {
        int count = 0;
        for (Card card : cards) {
            if (card.getTypeofCard() == type) {
                count++;
            }
        }
        return count;
    }

    private void playFromHand(CardType type) {
        requireStarted();
        Player current = getCurrentPlayer();
        if (countOf(current.getHand(), type) == 0) {
            throw new IllegalStateException(NOT_IN_HAND);
        }
        discardPile.add(current.removeCardOfType(type));
    }
}