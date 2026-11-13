package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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

	@Test
	void getDrawPile_returnedListIsDefensiveCopy() {
		GameManager manager = new GameManager();
		manager.startNewGame(List.of("A", "B"));
		int pileSize = manager.getDrawPile().size();

		manager.getDrawPile().clear();

		assertEquals(pileSize, manager.getDrawPile().size());
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

	@Test
	void getDrawPile_twoPlayers_hasOneKittenAndFourDefuse() {
		GameManager manager = new GameManager();

		manager.startNewGame(List.of("A", "B"));

		List<Card> pile = manager.getDrawPile();
		assertEquals(1, countType(pile, CardType.Exploding_Kitten));
		assertEquals(4, countType(pile, CardType.Defuse));
	}

	@Test
	void getDrawPile_fourPlayers_hasThreeKittensAndTwoDefuse() {
		GameManager manager = new GameManager();

		manager.startNewGame(List.of("A", "B", "C", "D"));

		List<Card> pile = manager.getDrawPile();
		assertEquals(3, countType(pile, CardType.Exploding_Kitten));
		assertEquals(2, countType(pile, CardType.Defuse));
	}

	@Test
	void playAttack_beforeStartNewGame_throwsIllegalStateException() {
		GameManager manager = new GameManager();

		IllegalStateException ex = assertThrows(
				IllegalStateException.class,
				manager::playAttack);
		assertEquals("gameManager.notStarted", ex.getMessage());
	}

	@Test
	void playAttack_noAttackInHand_throwsAndNothingChanges() {
		GameManager manager = new GameManager(alwaysFirst());
		manager.startNewGame(List.of("A", "B", "C"));
		List<Card> handBefore = manager.getCurrentPlayer().getHand();

		IllegalStateException ex = assertThrows(
				IllegalStateException.class,
				manager::playAttack);
		assertEquals("gameManager.play.notInHand", ex.getMessage());
		assertEquals("A", manager.getCurrentPlayer().getName());
		assertEquals(handBefore, manager.getCurrentPlayer().getHand());
	}

	@Test
	void getDiscardPile_beforeStart_isEmpty() {
		GameManager manager = new GameManager();
		assertTrue(manager.getDiscardPile().isEmpty());
	}

	@Test
	void getDiscardPile_afterStartThreePlayers_isEmpty() {
		GameManager manager = new GameManager(alwaysFirst());
		manager.startNewGame(List.of("A", "B", "C"));

		assertTrue(manager.getDiscardPile().isEmpty());
	}

	@Test
	void getTurnsRemaining_beforeStart_throwsIllegalStateException() {
		GameManager manager = new GameManager();

		IllegalStateException ex = assertThrows(
				IllegalStateException.class,
				manager::getTurnsRemaining);
		assertEquals("gameManager.notStarted", ex.getMessage());
	}

	@Test
	void getTurnsRemaining_afterStartFourPlayers_isOne() {
		GameManager manager = new GameManager(alwaysFirst());
		manager.startNewGame(List.of("A", "B", "C", "D"));

		assertEquals(1, manager.getTurnsRemaining());
	}

	@Test
	void playAttack_twoPlayers_attackDiscardedAndNextPlayerOwesTwo() {
		GameManager manager = new GameManager(alwaysFirst());
		manager.startNewGame(List.of("A", "B"));
		Card attack = new Card(CardType.Attack);
		manager.getCurrentPlayer().addCard(attack);

		manager.playAttack();

		Player a = manager.getPlayers().get(0);
		assertEquals(0, countType(a.getHand(), CardType.Attack));
		List<Card> discard = manager.getDiscardPile();
		assertSame(attack, discard.get(discard.size() - 1));
		assertEquals("B", manager.getCurrentPlayer().getName());
		assertEquals(2, manager.getTurnsRemaining());
	}

	@Test
	void getDiscardPile_returnedListIsDefensiveCopy() {
		GameManager manager = new GameManager(alwaysFirst());
		manager.startNewGame(List.of("A", "B"));
		manager.getCurrentPlayer().addCard(new Card(CardType.Attack));
		manager.playAttack();

		manager.getDiscardPile().clear();

		assertEquals(1, manager.getDiscardPile().size());
	}

	@Test
	void startNewGame_afterAttack_discardEmptyAndOneTurn() {
		GameManager manager = new GameManager(alwaysFirst());
		manager.startNewGame(List.of("A", "B"));
		manager.getCurrentPlayer().addCard(new Card(CardType.Attack));
		manager.playAttack();

		manager.startNewGame(List.of("A", "B"));

		assertTrue(manager.getDiscardPile().isEmpty());
		assertEquals(1, manager.getTurnsRemaining());
	}

	@Test
	void playAttack_fourPlayersFromFirstSeat_nextPlayerIsB() {
		GameManager manager = new GameManager(alwaysFirst());
		manager.startNewGame(List.of("A", "B", "C", "D"));
		manager.getCurrentPlayer().addCard(new Card(CardType.Attack));

		manager.playAttack();

		assertEquals("B", manager.getCurrentPlayer().getName());
		assertEquals(2, manager.getTurnsRemaining());
	}

	@Test
	void playAttack_fourPlayersFromLastSeat_wrapsToA() {
		GameManager manager = new GameManager(alwaysLast());
		manager.startNewGame(List.of("A", "B", "C", "D"));
		manager.getCurrentPlayer().addCard(new Card(CardType.Attack));

		manager.playAttack();

		assertEquals("A", manager.getCurrentPlayer().getName());
		assertEquals(2, manager.getTurnsRemaining());
	}
}