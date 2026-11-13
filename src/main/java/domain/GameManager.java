package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

	public GameManager() {
		this(new Random());
	}

	GameManager(Random random) {
		this.random = random;
	}

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

	public List<Card> getDrawPile() {
		List<Card> pile = new ArrayList<>();
		for (int i = 0; i < deck.getDeckSize(); i++) {
			pile.add(deck.getCardAt(i));
		}
		return pile;
	}

	public void playAttack() {
		requireStarted();
		Player current = getCurrentPlayer();
		if (countOf(current.getHand(), CardType.Attack) == 0) {
			throw new IllegalStateException(NOT_IN_HAND);
		}
		discardPile.add(current.removeCardOfType(CardType.Attack));
		turnManager.attackNextPlayer();
	}

	public List<Card> getDiscardPile() {
		return new ArrayList<>(discardPile);
	}

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
}