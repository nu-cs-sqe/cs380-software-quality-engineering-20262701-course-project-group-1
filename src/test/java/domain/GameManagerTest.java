package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

		assertEquals(List.of("A", "B"), manager.getPlayers());
	}

	@Test
	void startNewGame_fourPlayers_storesNames() {
		GameManager manager = new GameManager();

		manager.startNewGame(List.of("A", "B", "C", "D"));

		assertEquals(List.of("A", "B", "C", "D"), manager.getPlayers());
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

		assertEquals(List.of("A", "B"), manager.getPlayers());
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

		assertEquals("A", manager.getCurrentPlayer());
	}

	@Test
	void getCurrentPlayer_twoPlayers_randomLast_returnsB() {
		GameManager manager = new GameManager(alwaysLast());

		manager.startNewGame(List.of("A", "B"));

		assertEquals("B", manager.getCurrentPlayer());
	}

	@Test
	void getCurrentPlayer_fourPlayers_randomFirst_returnsA() {
		GameManager manager = new GameManager(alwaysFirst());

		manager.startNewGame(List.of("A", "B", "C", "D"));

		assertEquals("A", manager.getCurrentPlayer());
	}

	@Test
	void getCurrentPlayer_fourPlayers_randomLast_returnsD() {
		GameManager manager = new GameManager(alwaysLast());

		manager.startNewGame(List.of("A", "B", "C", "D"));

		assertEquals("D", manager.getCurrentPlayer());
	}
}