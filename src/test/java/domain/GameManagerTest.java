package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

class GameManagerTest {

	@Test
	void constructor_getPlayers_isEmpty() {
		GameManager manager = new GameManager();
		assertTrue(manager.getPlayers().isEmpty());
	}

	@Test
	void startNewGame_onePlayer_throwsIllegalArgumentException() {
		GameManager manager = new GameManager();

		IllegalArgumentException ex = assertThrows(
				IllegalArgumentException.class,
				() -> manager.startNewGame(List.of("A")));
		assertEquals("gameManager.playerCount.outOfRange", ex.getMessage());
		assertTrue(manager.getPlayers().isEmpty());
	}

	@Test
	void startNewGame_zeroPlayers_throwsIllegalArgumentException() {
		GameManager manager = new GameManager();

		IllegalArgumentException ex = assertThrows(
				IllegalArgumentException.class,
				() -> manager.startNewGame(List.of()));
		assertEquals("gameManager.playerCount.outOfRange", ex.getMessage());
	}

	@Test
	void startNewGame_fivePlayers_throwsIllegalArgumentException() {
		GameManager manager = new GameManager();

		IllegalArgumentException ex = assertThrows(
				IllegalArgumentException.class,
				() -> manager.startNewGame(List.of("A", "B", "C", "D", "E")));
		assertEquals("gameManager.playerCount.outOfRange", ex.getMessage());
	}
}
