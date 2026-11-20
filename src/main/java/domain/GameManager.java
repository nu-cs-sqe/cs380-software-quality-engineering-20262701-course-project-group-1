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
    private static final String NO_KITTEN = "gameManager.defuse.noKitten";
    private static final String NOT_OVER = "gameManager.notOver";
    private static final String MUST_FINISH_DRAWING = "gameManager.play.mustFinishDrawing";
    private static final String INVALID_REINSERT_INDEX = "gameManager.defuse.invalidIndex";
    private static final int TOTAL_DEFUSES = 6;
    private static final int STARTING_GENERIC_CARDS = 5;
    private static final int SEE_THE_FUTURE_SUPPLY = 10;
    private static final int ATTACK_SUPPLY = 5;
    private static final int SHUFFLE_SUPPLY = 5;
    private static final int GAMBLE_CAT_SUPPLY = 4;
    private static final int SEE_THE_FUTURE_COUNT = 3;
    private static final int GAMBLE_CHOICES = 2;

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
        validatePlayerNames(playerNames);
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
        return deck.getCards();
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

    /**
     * Plays a See the Future card from the current player's hand and shows them the top three
     * cards of the draw pile.
     *
     * <p>The draw pile is not changed and the current player keeps their turn.
     *
     * @return the top three cards of the draw pile, top card first
     * @throws IllegalStateException if the game has not started or the current player has no
     *     See the Future card
     */
    public List<Card> playSeeTheFuture() {
        playFromHand(CardType.See_The_Future);
        return deck.peekTopofDeck(SEE_THE_FUTURE_COUNT);
    }

    /**
     * Draws the top card of the draw pile for the current player.
     *
     * <p>A normal card goes into the hand and the turn ends. An Exploding Kitten stays in the
     * hand if the player has a Defuse, and the turn waits for it to be played. Without a Defuse,
     * the player is eliminated, the Exploding Kitten goes to the discard pile, and the next
     * living player becomes current.
     *
     * @return the card that was drawn
     * @throws IllegalStateException if the game has not started
     */
    public Card drawCard() {
        requireStarted();
        Card card = deck.drawCard();
        Player current = getCurrentPlayer();
        if (card.getTypeofCard() != CardType.Exploding_Kitten) {
            current.addCard(card);
            turnManager.endTurn();
        } else if (current.hasCard(CardType.Defuse)) {
            current.addCard(card);
        } else {
            current.eliminate();
            discardPile.add(card);
            turnManager.eliminateCurrentPlayer();
        }
        return card;
    }

    /**
     * Plays a Defuse after the current player has drawn an Exploding Kitten.
     *
     * <p>The Defuse goes to the discard pile, the Exploding Kitten is put back into the draw pile
     * at the chosen index, and the turn ends the same way a normal draw does.
     *
     * @param reinsertIndex where to put the Exploding Kitten, from 0 (top) to the draw-pile size
     *     (bottom)
     * @throws IllegalStateException if the game has not started, the current player has no
     *     Exploding Kitten, or the current player has no Defuse
     * @throws IllegalArgumentException if {@code reinsertIndex} is out of range
     */
    public void playDefuse(int reinsertIndex) {
        requireStarted();
        Player current = getCurrentPlayer();
        if (!current.hasCard(CardType.Exploding_Kitten)) {
            throw new IllegalStateException(NO_KITTEN);
        }
        if (!current.hasCard(CardType.Defuse)) {
            throw new IllegalStateException(NOT_IN_HAND);
        }
        if (reinsertIndex < 0 || reinsertIndex > deck.getDeckSize()) {
            throw new IllegalArgumentException(INVALID_REINSERT_INDEX);
        }
        Card kitten = current.removeCardOfType(CardType.Exploding_Kitten);
        discardPile.add(current.removeCardOfType(CardType.Defuse));
        deck.insertCardAt(kitten, reinsertIndex);
        turnManager.endTurn();
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

    /**
     * Plays a Gamble Cat from the current player's hand and flips a coin.
     *
     * <p>Lucky: the turn ends immediately without drawing, like a Skip. Unlucky: the turn only
     * ends after two draws in a row, and no cards can be played between them.
     *
     * @return true if the turn ended
     * @throws IllegalStateException if the game has not started or the current player has no
     *     Gamble Cat
     */
    public boolean playGambleCat() {
        playFromHand(CardType.Gamble_Cat);
        if (random.nextInt(GAMBLE_CHOICES) == 0) {
            turnManager.endTurn();
            return true;
        }
        turnManager.addExtraDraw();
        return false;
    }

    /**
     * Returns whether the game has ended.
     *
     * @return true when at most one player is still alive
     * @throws IllegalStateException if the game has not started
     */
    public boolean isGameOver() {
        requireStarted();
        return countAlive() <= 1;
    }

    /**
     * Returns the only remaining living player.
     *
     * @return the winner
     * @throws IllegalStateException if the game has not started or is not over
     */
    public Player getWinner() {
        requireStarted();
        if (!isGameOver()) {
            throw new IllegalStateException(NOT_OVER);
        }
        for (Player player : players) {
            if (player.isAlive()) {
                return player;
            }
        }
        throw new IllegalStateException(NOT_OVER);
    }

    private static void validatePlayerNames(List<String> playerNames) {
        if (playerNames.size() < GameRules.MIN_PLAYERS
                || playerNames.size() > GameRules.MAX_PLAYERS) {
            throw new IllegalArgumentException(PLAYER_COUNT_OUT_OF_RANGE);
        }
        for (String name : playerNames) {
            if (name.isBlank()) {
                throw new IllegalArgumentException(PLAYER_NAME_BLANK);
            }
        }
    }

    private void dealStartingHands() {
        deck = new Deck(startingCards(), random);
        List<Card> defuses = deck.removeAllCardsofType(CardType.Defuse);
        deck.shuffleDeck();
        dealHands(defuses);
        returnLeftoverDefuses(defuses);
        addExplodingKittens(players.size() - 1);
        deck.shuffleDeck();
    }

    private void dealHands(List<Card> defuses) {
        for (Player player : players) {
            for (int i = 0; i < STARTING_GENERIC_CARDS; i++) {
                player.addCard(deck.drawCard());
            }
            player.addCard(defuses.remove(0));
        }
    }

    private void returnLeftoverDefuses(List<Card> defuses) {
        for (Card defuse : defuses) {
            deck.addCard(defuse);
        }
    }

    private void addExplodingKittens(int count) {
        for (int i = 0; i < count; i++) {
            deck.addCard(new Card(CardType.Exploding_Kitten));
        }
    }

    private static List<Card> startingCards() {
        List<Card> cards = new ArrayList<>();
        for (int i = 0; i < SEE_THE_FUTURE_SUPPLY; i++) {
            cards.add(new Card(CardType.See_The_Future));
        }
        for (int i = 0; i < ATTACK_SUPPLY; i++) {
            cards.add(new Card(CardType.Attack));
        }
        for (int i = 0; i < SHUFFLE_SUPPLY; i++) {
            cards.add(new Card(CardType.Shuffle));
        }
        for (int i = 0; i < GAMBLE_CAT_SUPPLY; i++) {
            cards.add(new Card(CardType.Gamble_Cat));
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

    private int countAlive() {
        int alive = 0;
        for (Player player : players) {
            if (player.isAlive()) {
                alive++;
            }
        }
        return alive;
    }

    private void playFromHand(CardType type) {
        requireStarted();
        if (turnManager.isMidDraw()) {
            throw new IllegalStateException(MUST_FINISH_DRAWING);
        }
        Player current = getCurrentPlayer();
        if (!current.hasCard(type)) {
            throw new IllegalStateException(NOT_IN_HAND);
        }
        discardPile.add(current.removeCardOfType(type));
    }

    // Used in GameManagerTest to put a known card on top of the draw pile
    void placeOnTopOfDrawPile(Card card) {
        deck.insertCardAt(card, 0);
    }
}
