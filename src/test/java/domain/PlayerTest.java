package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class PlayerTest {

	@Test
	void constructor_setsNameAliveAndEmptyHand() {
		Player player = new Player("A");

		assertEquals("A", player.getName());
		assertTrue(player.isAlive());
		assertTrue(player.getHand().isEmpty());
	}
}
