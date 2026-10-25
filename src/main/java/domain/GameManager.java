package domain;

import java.util.ArrayList;
import java.util.List;

public class GameManager {

	private static final String PLAYER_COUNT_OUT_OF_RANGE = "gameManager.playerCount.outOfRange";

	public void startNewGame(List<String> playerNames) {
		if (playerNames.size() < 2) {
			throw new IllegalArgumentException(PLAYER_COUNT_OUT_OF_RANGE);
		}
	}

	public List<String> getPlayers() {
		return new ArrayList<>();
	}
}
