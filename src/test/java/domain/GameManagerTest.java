package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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

	@Test
	void startNewGame_twoPlayers_storesNames() {
		GameManager manager = new GameManager();

		manager.startNewGame(List.of("A", "B"));

		assertEquals(List.of("A", "B"), namesOf(manager.getPlayers()));
		assertPlayersAlive(manager.getPlayers());
		assertStartingHands(manager.getPlayers());
		assertDrawPile(manager, 1, 4);
	}

	@Test
	void startNewGame_fourPlayers_storesNames() {
		GameManager manager = new GameManager();

		manager.startNewGame(List.of("A", "B", "C", "D"));

		assertEquals(List.of("A", "B", "C", "D"), namesOf(manager.getPlayers()));
		assertPlayersAlive(manager.getPlayers());
		assertStartingHands(manager.getPlayers());
		assertDrawPile(manager, 3, 2);
	}

	@Test
	void startNewGame_blankName_throwsIllegalArgumentException() {
		GameManager manager = new GameManager();

		IllegalArgumentException ex = assertThrows(
				IllegalArgumentException.class,
				() -> manager.startNewGame(List.of("", "B")));
		assertEquals("gameManager.playerName.blank", ex.getMessage());
		assertTrue(manager.getPlayers().isEmpty());
	}

	@Test
	void getPlayers_returnedListIsDefensiveCopy() {
		GameManager manager = new GameManager();
		manager.startNewGame(List.of("A", "B"));

		manager.getPlayers().clear();

		assertEquals(List.of("A", "B"), namesOf(manager.getPlayers()));
	}

	private static Random alwaysFirst() {
		return new Random() {
			@Override
			public int nextInt(int bound) {
				return 0;
			}
		};
	}

	private static Random alwaysLast() {
		return new Random() {
			@Override
			public int nextInt(int bound) {
				return bound - 1;
			}
		};
	}

	@Test
	void getCurrentPlayer_twoPlayers_randomFirst_returnsA() {
		GameManager manager = new GameManager(alwaysFirst());

		manager.startNewGame(List.of("A", "B"));

		assertEquals("A", manager.getCurrentPlayer().getName());
	}

	@Test
	void getCurrentPlayer_twoPlayers_randomLast_returnsB() {
		GameManager manager = new GameManager(alwaysLast());

		manager.startNewGame(List.of("A", "B"));

		assertEquals("B", manager.getCurrentPlayer().getName());
	}

	@Test
	void getCurrentPlayer_fourPlayers_randomFirst_returnsA() {
		GameManager manager = new GameManager(alwaysFirst());

		manager.startNewGame(List.of("A", "B", "C", "D"));

		assertEquals("A", manager.getCurrentPlayer().getName());
	}

	@Test
	void getCurrentPlayer_fourPlayers_randomLast_returnsD() {
		GameManager manager = new GameManager(alwaysLast());

		manager.startNewGame(List.of("A", "B", "C", "D"));

		assertEquals("D", manager.getCurrentPlayer().getName());
	}

	private static List<String> namesOf(List<Player> players) {
		List<String> names = new ArrayList<>();
		for (Player player : players) {
			names.add(player.getName());
		}
		return names;
	}

	private static void assertPlayersAlive(List<Player> players) {
		for (Player player : players) {
			assertTrue(player.isAlive());
		}
	}

	private static void assertStartingHands(List<Player> players) {
		for (Player player : players) {
			List<Card> hand = player.getHand();
			assertEquals(6, hand.size());
			assertEquals(1, countType(hand, CardType.Defuse));
			assertEquals(5, countType(hand, CardType.See_The_Future));
			assertEquals(0, countType(hand, CardType.Exploding_Kitten));
		}
	}

	private static void assertDrawPile(GameManager manager, int explodingKittens, int defuses) {
		List<Card> pile = manager.getDrawPile();
		assertEquals(explodingKittens, countType(pile, CardType.Exploding_Kitten));
		assertEquals(defuses, countType(pile, CardType.Defuse));
	}

	private static int countType(List<Card> cards, CardType type) {
		int count = 0;
		for (Card card : cards) {
			if (card.getTypeofCard() == type) {
				count++;
			}
		}
		return count;
	}
}