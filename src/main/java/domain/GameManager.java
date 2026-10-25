package domain;

import java.util.ArrayList;
import java.util.List;

public class GameManager {

	private static final String PLAYER_COUNT_OUT_OF_RANGE = "gameManager.playerCount.outOfRange";
	private static final String PLAYER_NAME_BLANK = "gameManager.playerName.blank";

	private final List<String> players = new ArrayList<>();

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
	}

	public List<String> getPlayers() {
		return players;
	}
}
