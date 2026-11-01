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

	@Test
	void getName_returnsNameGivenAtConstruction() {
		Player player = new Player("B");

		assertEquals("B", player.getName());
	}

	@Test
	void addCard_onEmptyHand_handSizeIsOne() {
		Player player = new Player("A");

		player.addCard(new Card(CardType.See_The_Future));

		assertEquals(1, player.getHand().size());
	}
}
