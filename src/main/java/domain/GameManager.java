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
    private static final List<CardType> SPECIAL_CATS = List.of(
            CardType.Special_Cat_1,
            CardType.Special_Cat_2,
            CardType.Special_Cat_3,
            CardType.Special_Cat_4);

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
        return alivePlayers().size() <= 1;
    }

    /**
     * Returns the only remaining living player.
     *
     * @return the winner
     * @throws IllegalStateException if the game has not started or is not over
     */
    public Player getWinner() {
        if (!isGameOver()) {
            throw new IllegalStateException(NOT_OVER);
        }
        return alivePlayers().get(0);
    }

    /**
     * Returns how many cards are left in the draw pile.
     *
     * @return the draw-pile size
     * @throws IllegalStateException if the game has not started
     */
    public int getDrawPileSize() {
        requireStarted();
        return deck.getDeckSize();
    }

    /**
     * Plays all four special cats from the current player's hand to eliminate the only other
     * living player, which wins the game.
     *
     * <p>Only allowed when exactly two players are alive. The four cats go to the discard pile.
     *
     * @return the player who was eliminated
     * @throws IllegalStateException if the game has not started, the current player is between
     *     the two draws of an unlucky Gamble Cat, more than two players are alive, or the current
     *     player does not hold all four special cats
     */
    public Player playSpecialCatSet() {
        requireStarted();
        Player current = getCurrentPlayer();
        for (CardType cat : SPECIAL_CATS) {
            discardPile.add(current.removeCardOfType(cat));
        }
        Player opponent = opponentOf(current);
        opponent.eliminate();
        return opponent;
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
        addSpecialCats();
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

    private void addSpecialCats() {
        for (CardType cat : SPECIAL_CATS) {
            deck.addCard(new Card(cat));
        }
    }

    private static List<Card> startingCards() {
        List<Card> cards = new ArrayList<>();
        addCopies(cards, CardType.See_The_Future, SEE_THE_FUTURE_SUPPLY);
        addCopies(cards, CardType.Attack, ATTACK_SUPPLY);
        addCopies(cards, CardType.Shuffle, SHUFFLE_SUPPLY);
        addCopies(cards, CardType.Gamble_Cat, GAMBLE_CAT_SUPPLY);
        addCopies(cards, CardType.Defuse, TOTAL_DEFUSES);
        return cards;
    }


    private void requireStarted() {
        if (players.isEmpty()) {
            throw new IllegalStateException(NOT_STARTED);
        }
    }

    private List<Player> alivePlayers() {
        List<Player> alive = new ArrayList<>();
        for (Player player : players) {
            if (player.isAlive()) {
                alive.add(player);
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

    private static void addCopies(List<Card> cards, CardType type, int count) {
        for (int i = 0; i < count; i++) {
            cards.add(new Card(type));
        }
    }

    private Player opponentOf(Player current) {
        List<Player> others = alivePlayers();
        others.remove(current);
        return others.get(0);
    }

    // Used in GameManagerTest to put a known card on top of the draw pile
    void placeOnTopOfDrawPile(Card card) {
        deck.insertCardAt(card, 0);
    }
}
