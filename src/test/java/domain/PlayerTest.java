package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

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

	@Test
	void getHand_returnedListIsDefensiveCopy() {
		Player player = new Player("A");
		player.addCard(new Card(CardType.See_The_Future));

		player.getHand().clear();

		assertEquals(1, player.getHand().size());
	}

	@Test
	void removeCardOfType_onlyCardMatches_returnsItAndHandIsEmpty() {
		Player player = new Player("A");
		Card defuse = new Card(CardType.Defuse);
		player.addCard(defuse);

		Card removed = player.removeCardOfType(CardType.Defuse);

		assertSame(defuse, removed);
		assertTrue(player.getHand().isEmpty());
	}

	@Test
	void removeCardOfType_matchNotFirst_removesFirstMatchOnly() {
		Player player = new Player("A");
		Card stf = new Card(CardType.See_The_Future);
		Card defuse1 = new Card(CardType.Defuse);
		Card defuse2 = new Card(CardType.Defuse);
		player.addCard(stf);
		player.addCard(defuse1);
		player.addCard(defuse2);

		Card removed = player.removeCardOfType(CardType.Defuse);

		assertSame(defuse1, removed);
		List<Card> hand = player.getHand();
		assertEquals(2, hand.size());
		assertSame(stf, hand.get(0));
		assertSame(defuse2, hand.get(1));
	}
}
