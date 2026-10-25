package domain;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class GameManagerTest {

	@Test
	void constructor_getPlayers_isEmpty() {
		GameManager manager = new GameManager();
		assertTrue(manager.getPlayers().isEmpty());
	}
}
