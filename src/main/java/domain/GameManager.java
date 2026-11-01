package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameManager {

	private static final String PLAYER_COUNT_OUT_OF_RANGE = "gameManager.playerCount.outOfRange";
	private static final String PLAYER_NAME_BLANK = "gameManager.playerName.blank";

	private final List<Player> players = new ArrayList<>();
	private final TurnManager turnManager = new TurnManager();
	private final Random random;

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
		for (String name : playerNames) {
			players.add(new Player(name));
		}
		turnManager.pickStart(players.size(), random);
	}

	public Player getCurrentPlayer() {
		return players.get(turnManager.getCurrentIndex());
	}

	public List<Player> getPlayers() {
		return new ArrayList<>(players);
	}
}