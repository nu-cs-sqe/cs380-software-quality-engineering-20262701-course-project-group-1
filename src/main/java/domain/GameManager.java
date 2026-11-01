package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameManager {

	private static final String PLAYER_COUNT_OUT_OF_RANGE = "gameManager.playerCount.outOfRange";
	private static final String PLAYER_NAME_BLANK = "gameManager.playerName.blank";

	private final List<String> players = new ArrayList<>();
	private final TurnManager turnManager = new TurnManager();
	private final Random random;
	private Deck drawPile;

	public GameManager() {
		this(new Random());
	}

	GameManager(Random random) {
		this.random = random;
	}

	public void startNewGame(List<String> playerNames) {
		if (playerNames.size() < 2 || playerNames.size() > 4) {
			throw new IllegalArgumentException(PLAYER_COUNT_OUT_OF_RANGE);
		}
		for (String name : playerNames) {
			if (name.isBlank()) {
				throw new IllegalArgumentException(PLAYER_NAME_BLANK);
			}
		}
		players.clear();
		players.addAll(playerNames);

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

	public String getCurrentPlayer() {
		return players.get(turnManager.getCurrentIndex());
	}


	public List<Card> getDrawPile() {
		return drawPile.peekTopofDeck(drawPile.getDeckSize());
	}

	public List<String> getPlayers() {
		return new ArrayList<>(players);
	}
}