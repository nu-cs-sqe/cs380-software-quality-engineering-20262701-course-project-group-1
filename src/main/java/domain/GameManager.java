package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameManager {

	private static final String PLAYER_COUNT_OUT_OF_RANGE = "gameManager.playerCount.outOfRange";
	private static final String PLAYER_NAME_BLANK = "gameManager.playerName.blank";
	private static final int MIN_PLAYERS = 2;
	private static final int MAX_PLAYERS = 4;
	private static final int TOTAL_DEFUSES = 6;
	private static final int STARTING_GENERIC_CARDS = 5;
	private static final int GENERIC_SUPPLY = 20;

	private final List<Player> players = new ArrayList<>();
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
		for (String name : playerNames) {
			players.add(new Player(name));
		}
		dealStartingHands();
		turnManager.pickStart(players.size(), random);

		List<Card> pileCards = new ArrayList<>();
		for (int i = 0; i < players.size() - 1; i++) {
			pileCards.add(new Card(CardType.Exploding_Kitten));
		}
		for (int i = 0; i < 6 - players.size(); i++) {
			pileCards.add(new Card(CardType.Defuse));
		}
		drawPile = new Deck(pileCards, random);
		drawPile.shuffleDeck();
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
}