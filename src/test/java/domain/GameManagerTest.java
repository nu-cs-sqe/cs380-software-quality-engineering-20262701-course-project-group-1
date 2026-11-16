package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.easymock.EasyMock;
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

    @Test
    void playAttack_twoAttacksInHand_removesOnlyOne() {
        GameManager manager = new GameManager(alwaysFirst());
        manager.startNewGame(List.of("A", "B", "C"));
        manager.getCurrentPlayer().addCard(new Card(CardType.Attack));
        manager.getCurrentPlayer().addCard(new Card(CardType.Attack));

        manager.playAttack();

        Player a = manager.getPlayers().get(0);
        assertEquals(1, countType(a.getHand(), CardType.Attack));
        assertEquals(1, countType(manager.getDiscardPile(), CardType.Attack));
    }

    @Test
    void playAttack_stackedAttackThreePlayers_passesToThirdPlayerOwingFour() {
        GameManager manager = new GameManager(alwaysFirst());
        manager.startNewGame(List.of("A", "B", "C"));
        manager.getCurrentPlayer().addCard(new Card(CardType.Attack));
        manager.playAttack();                                   // B, owes 2
        manager.getCurrentPlayer().addCard(new Card(CardType.Attack));

        manager.playAttack();

        assertEquals("C", manager.getCurrentPlayer().getName());
        assertEquals(4, manager.getTurnsRemaining());
        assertEquals(2, countType(manager.getDiscardPile(), CardType.Attack));
    }

    @Test
    void playShuffle_beforeStartNewGame_throwsIllegalStateException() {
        GameManager manager = new GameManager();

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                manager::playShuffle);
        assertEquals("gameManager.notStarted", ex.getMessage());
    }

    @Test
    void playShuffle_noShuffleInHand_throwsAndNothingChanges() {
        GameManager manager = new GameManager(alwaysFirst());
        manager.startNewGame(List.of("A", "B", "C"));
        List<Card> handBefore = manager.getCurrentPlayer().getHand();
        List<Card> pileBefore = manager.getDrawPile();

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                manager::playShuffle);
        assertEquals("gameManager.play.notInHand", ex.getMessage());
        assertEquals(handBefore, manager.getCurrentPlayer().getHand());
        assertEquals(pileBefore, manager.getDrawPile());
        assertTrue(manager.getDiscardPile().isEmpty());
    }

    @Test
    void playShuffle_twoPlayers_shuffleDiscardedAndDrawPileShuffled() {
        GameManager manager = new GameManager(alwaysFirst());
        manager.startNewGame(List.of("A", "B"));
        Card shuffle = new Card(CardType.Shuffle);
        manager.getCurrentPlayer().addCard(shuffle);
        final List<Card> pileBefore = manager.getDrawPile();

        manager.playShuffle();

        Player a = manager.getPlayers().get(0);
        assertEquals(0, countType(a.getHand(), CardType.Shuffle));
        List<Card> discard = manager.getDiscardPile();
        assertSame(shuffle, discard.get(discard.size() - 1));

        List<Card> pileAfter = manager.getDrawPile();
        int size = pileBefore.size();
        assertEquals(size, pileAfter.size());
        for (int i = 0; i < size - 1; i++) {
            assertSame(pileBefore.get(i + 1), pileAfter.get(i));
        }
        assertSame(pileBefore.get(0), pileAfter.get(size - 1));
    }

    @Test
    void playShuffle_fourPlayersFromLastSeat_turnDoesNotChange() {
        GameManager manager = new GameManager(alwaysLast());
        manager.startNewGame(List.of("A", "B", "C", "D"));
        manager.getCurrentPlayer().addCard(new Card(CardType.Shuffle));

        manager.playShuffle();

        assertEquals("D", manager.getCurrentPlayer().getName());
        assertEquals(1, manager.getTurnsRemaining());
    }

    @Test
    void playShuffle_twoShufflesInHand_removesOnlyOne() {
        GameManager manager = new GameManager(alwaysFirst());
        manager.startNewGame(List.of("A", "B", "C"));
        manager.getCurrentPlayer().addCard(new Card(CardType.Shuffle));
        manager.getCurrentPlayer().addCard(new Card(CardType.Shuffle));

        manager.playShuffle();

        Player a = manager.getPlayers().get(0);
        assertEquals(1, countType(a.getHand(), CardType.Shuffle));
        assertEquals(1, countType(manager.getDiscardPile(), CardType.Shuffle));
    }

    @Test
    void playShuffle_whileUnderAttack_turnsRemainingUnchanged() {
        GameManager manager = new GameManager(alwaysFirst());
        manager.startNewGame(List.of("A", "B"));
        manager.getCurrentPlayer().addCard(new Card(CardType.Attack));
        manager.playAttack();                                   // B, owes 2
        manager.getCurrentPlayer().addCard(new Card(CardType.Shuffle));

        manager.playShuffle();

        assertEquals("B", manager.getCurrentPlayer().getName());
        assertEquals(2, manager.getTurnsRemaining());
    }

    @Test
    void playSeeTheFuture_beforeStartNewGame_throwsIllegalStateException() {
        GameManager manager = new GameManager();

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                manager::playSeeTheFuture);
        assertEquals("gameManager.notStarted", ex.getMessage());
    }

    @Test
    void playSeeTheFuture_noSeeTheFutureInHand_throwsAndNothingChanges() {
        GameManager manager = new GameManager(alwaysFirst());
        manager.startNewGame(List.of("A", "B", "C"));
        Player a = manager.getCurrentPlayer();
        while (countType(a.getHand(), CardType.See_The_Future) > 0) {
            a.removeCardOfType(CardType.See_The_Future);
        }
        final List<Card> handBefore = a.getHand();
        final List<Card> pileBefore = manager.getDrawPile();

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                manager::playSeeTheFuture);
        assertEquals("gameManager.play.notInHand", ex.getMessage());
        assertEquals(handBefore, a.getHand());
        assertEquals(pileBefore, manager.getDrawPile());
        assertTrue(manager.getDiscardPile().isEmpty());
    }

    @Test
    void playSeeTheFuture_twoPlayers_returnsTopThreeAndPileUnchanged() {
        GameManager manager = new GameManager(alwaysFirst());
        manager.startNewGame(List.of("A", "B"));
        final List<Card> pileBefore = manager.getDrawPile();

        List<Card> seen = manager.playSeeTheFuture();

        assertEquals(3, seen.size());
        for (int i = 0; i < 3; i++) {
            assertSame(pileBefore.get(i), seen.get(i));
        }
        assertEquals(pileBefore, manager.getDrawPile());
        assertEquals(1, countType(manager.getDiscardPile(), CardType.See_The_Future));
        Player a = manager.getPlayers().get(0);
        assertEquals(4, countType(a.getHand(), CardType.See_The_Future));
    }

    @Test
    void playSeeTheFuture_fourPlayersFromLastSeat_turnDoesNotChange() {
        GameManager manager = new GameManager(alwaysLast());
        manager.startNewGame(List.of("A", "B", "C", "D"));
        final List<Card> pileBefore = manager.getDrawPile();

        List<Card> seen = manager.playSeeTheFuture();

        assertEquals(5, pileBefore.size());
        for (int i = 0; i < 3; i++) {
            assertSame(pileBefore.get(i), seen.get(i));
        }
        assertEquals("D", manager.getCurrentPlayer().getName());
        assertEquals(1, manager.getTurnsRemaining());
    }

    @Test
    void playSeeTheFuture_whileUnderAttack_turnsRemainingUnchanged() {
        GameManager manager = new GameManager(alwaysFirst());
        manager.startNewGame(List.of("A", "B"));
        manager.getCurrentPlayer().addCard(new Card(CardType.Attack));
        manager.playAttack();                                   // B, owes 2

        manager.playSeeTheFuture();

        assertEquals("B", manager.getCurrentPlayer().getName());
        assertEquals(2, manager.getTurnsRemaining());
    }

    @Test
    void playSeeTheFuture_returnedListIsDefensiveCopy() {
        GameManager manager = new GameManager(alwaysFirst());
        manager.startNewGame(List.of("A", "B"));
        final List<Card> pileBefore = manager.getDrawPile();

        manager.playSeeTheFuture().clear();

        assertEquals(pileBefore, manager.getDrawPile());
    }

    @Test
    void drawCard_beforeStartNewGame_throwsIllegalStateException() {
        GameManager manager = new GameManager();

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                manager::drawCard);
        assertEquals("gameManager.notStarted", ex.getMessage());
    }

    @Test
    void drawCard_twoPlayers_cardGoesToHandAndTurnPasses() {
        GameManager manager = new GameManager(alwaysFirst());
        manager.startNewGame(List.of("A", "B"));
        Card top = new Card(CardType.See_The_Future);
        manager.placeOnTopOfDrawPile(top);
        int pileBefore = manager.getDrawPile().size();
        Player a = manager.getCurrentPlayer();

        Card drawn = manager.drawCard();

        assertSame(top, drawn);
        assertTrue(a.getHand().contains(top));
        assertEquals(pileBefore - 1, manager.getDrawPile().size());
        assertEquals("B", manager.getCurrentPlayer().getName());
    }

    @Test
    void drawCard_whileUnderAttack_samePlayerOwesOneMoreTurn() {
        GameManager manager = new GameManager(alwaysFirst());
        manager.startNewGame(List.of("A", "B"));
        manager.getCurrentPlayer().addCard(new Card(CardType.Attack));
        manager.playAttack();
        manager.placeOnTopOfDrawPile(new Card(CardType.See_The_Future));

        manager.drawCard();

        assertEquals("B", manager.getCurrentPlayer().getName());
        assertEquals(1, manager.getTurnsRemaining());
    }


    @Test
    void drawCard_fourPlayersFromLastSeat_wrapsToA() {
        GameManager manager = new GameManager(alwaysLast());
        manager.startNewGame(List.of("A", "B", "C", "D"));
        manager.placeOnTopOfDrawPile(new Card(CardType.See_The_Future));

        manager.drawCard();

        assertEquals("A", manager.getCurrentPlayer().getName());
    }

    @Test
    void drawCard_explodingKittenWithDefuse_kittenInHandAndTurnWaits() {
        GameManager manager = new GameManager(alwaysFirst());
        manager.startNewGame(List.of("A", "B"));
        Card kitten = new Card(CardType.Exploding_Kitten);
        manager.placeOnTopOfDrawPile(kitten);
        Player a = manager.getCurrentPlayer();

        manager.drawCard();

        assertTrue(a.getHand().contains(kitten));
        assertTrue(a.isAlive());
        assertEquals("A", manager.getCurrentPlayer().getName());
    }

    @Test
    void drawCard_explodingKittenWithoutDefuse_playerEliminatedAndKittenDiscarded() {
        GameManager manager = new GameManager(alwaysFirst());
        manager.startNewGame(List.of("A", "B"));
        Player a = manager.getCurrentPlayer();
        a.removeCardOfType(CardType.Defuse);
        Card kitten = new Card(CardType.Exploding_Kitten);
        manager.placeOnTopOfDrawPile(kitten);

        manager.drawCard();

        assertFalse(a.isAlive());
        List<Card> discard = manager.getDiscardPile();
        assertSame(kitten, discard.get(discard.size() - 1));
        assertFalse(a.getHand().contains(kitten));
    }

    @Test
    void playDefuse_beforeStartNewGame_throwsIllegalStateException() {
        GameManager manager = new GameManager();

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> manager.playDefuse(0));
        assertEquals("gameManager.notStarted", ex.getMessage());
    }

    @Test
    void playDefuse_noKittenInHand_throwsAndNothingChanges() {
        GameManager manager = new GameManager(alwaysFirst());
        manager.startNewGame(List.of("A", "B"));
        List<Card> handBefore = manager.getCurrentPlayer().getHand();
        List<Card> pileBefore = manager.getDrawPile();

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> manager.playDefuse(0));
        assertEquals("gameManager.defuse.noKitten", ex.getMessage());
        assertEquals(handBefore, manager.getCurrentPlayer().getHand());
        assertEquals(pileBefore, manager.getDrawPile());
        assertTrue(manager.getDiscardPile().isEmpty());
    }

    @Test
    void playDefuse_afterDrawingKitten_reinsertsAtTopAndEndsTurn() {
        GameManager manager = new GameManager(alwaysFirst());
        manager.startNewGame(List.of("A", "B"));
        Card kitten = new Card(CardType.Exploding_Kitten);
        manager.placeOnTopOfDrawPile(kitten);
        Player a = manager.getCurrentPlayer();
        manager.drawCard();

        manager.playDefuse(0);

        assertFalse(a.getHand().contains(kitten));
        assertEquals(0, countType(a.getHand(), CardType.Defuse));
        List<Card> discard = manager.getDiscardPile();
        assertEquals(CardType.Defuse, discard.get(discard.size() - 1).getTypeofCard());
        assertSame(kitten, manager.getDrawPile().get(0));
        assertEquals("B", manager.getCurrentPlayer().getName());
    }

    @Test
    void playDefuse_afterDrawingKitten_reinsertsAtBottomAndEndsTurn() {
        GameManager manager = new GameManager(alwaysFirst());
        manager.startNewGame(List.of("A", "B"));
        Card kitten = new Card(CardType.Exploding_Kitten);
        manager.placeOnTopOfDrawPile(kitten);
        manager.drawCard();
        int bottomIndex = manager.getDrawPile().size();

        manager.playDefuse(bottomIndex);

        List<Card> pile = manager.getDrawPile();
        assertSame(kitten, pile.get(pile.size() - 1));
        List<Card> discard = manager.getDiscardPile();
        assertEquals(CardType.Defuse, discard.get(discard.size() - 1).getTypeofCard());
        assertEquals("B", manager.getCurrentPlayer().getName());
    }

    @Test
    void playDefuse_whileUnderAttack_samePlayerOwesOneMoreTurn() {
        GameManager manager = new GameManager(alwaysFirst());
        manager.startNewGame(List.of("A", "B"));
        manager.getCurrentPlayer().addCard(new Card(CardType.Attack));
        manager.playAttack();
        manager.placeOnTopOfDrawPile(new Card(CardType.Exploding_Kitten));
        manager.drawCard();

        manager.playDefuse(0);

        assertEquals("B", manager.getCurrentPlayer().getName());
        assertEquals(1, manager.getTurnsRemaining());
    }

    @Test
    void playDefuse_fourPlayersFromLastSeat_wrapsToA() {
        GameManager manager = new GameManager(alwaysLast());
        manager.startNewGame(List.of("A", "B", "C", "D"));
        manager.placeOnTopOfDrawPile(new Card(CardType.Exploding_Kitten));
        manager.drawCard();

        manager.playDefuse(0);

        assertEquals("A", manager.getCurrentPlayer().getName());
    }

    @Test
    void playDefuse_twoDefusesInHand_removesExactlyOne() {
        GameManager manager = new GameManager(alwaysFirst());
        manager.startNewGame(List.of("A", "B"));
        Player a = manager.getCurrentPlayer();
        a.addCard(new Card(CardType.Defuse));
        manager.placeOnTopOfDrawPile(new Card(CardType.Exploding_Kitten));
        manager.drawCard();

        manager.playDefuse(0);

        assertEquals(1, countType(a.getHand(), CardType.Defuse));
        assertEquals(1, countType(manager.getDiscardPile(), CardType.Defuse));
    }

    @Test
    void playDefuse_invalidReinsertIndex_throwsAndNothingChanges() {
        GameManager manager = new GameManager(alwaysFirst());
        manager.startNewGame(List.of("A", "B"));
        Card kitten = new Card(CardType.Exploding_Kitten);
        manager.placeOnTopOfDrawPile(kitten);
        Player a = manager.getCurrentPlayer();
        manager.drawCard();
        List<Card> handBefore = a.getHand();

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> manager.playDefuse(-1));
        assertEquals("gameManager.defuse.invalidIndex", ex.getMessage());
        assertEquals(handBefore, a.getHand());
        assertTrue(a.getHand().contains(kitten));
        assertEquals(1, countType(a.getHand(), CardType.Defuse));
        assertTrue(manager.getDiscardPile().isEmpty());
    }

    @Test
    void playDefuse_noDefuseInHand_throwsAndKittenStays() {
        GameManager manager = new GameManager(alwaysFirst());
        manager.startNewGame(List.of("A", "B"));
        Card kitten = new Card(CardType.Exploding_Kitten);
        manager.placeOnTopOfDrawPile(kitten);
        Player a = manager.getCurrentPlayer();
        manager.drawCard();
        a.removeCardOfType(CardType.Defuse);

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> manager.playDefuse(0));
        assertEquals("gameManager.play.notInHand", ex.getMessage());
        assertTrue(a.getHand().contains(kitten));
        assertTrue(manager.getDiscardPile().isEmpty());
    }

    @Test
    void playGambleCat_beforeStartNewGame_throwsIllegalStateException() {
        GameManager manager = new GameManager();

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                manager::playGambleCat);
        assertEquals("gameManager.notStarted", ex.getMessage());
    }

    @Test
    void playGambleCat_noGambleCatInHand_throwsAndNothingChanges() {
        GameManager manager = new GameManager(alwaysFirst());
        manager.startNewGame(List.of("A", "B", "C"));
        final List<Card> handBefore = manager.getCurrentPlayer().getHand();
        final List<Card> pileBefore = manager.getDrawPile();

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                manager::playGambleCat);
        assertEquals("gameManager.play.notInHand", ex.getMessage());
        assertEquals("A", manager.getCurrentPlayer().getName());
        assertEquals(handBefore, manager.getCurrentPlayer().getHand());
        assertEquals(pileBefore, manager.getDrawPile());
        assertTrue(manager.getDiscardPile().isEmpty());
    }

    @Test
    void playGambleCat_twoPlayersLucky_turnEndsWithoutDrawing() {
        GameManager manager = new GameManager(alwaysFirst());
        manager.startNewGame(List.of("A", "B"));
        manager.getCurrentPlayer().addCard(new Card(CardType.Gamble_Cat));
        final List<Card> pileBefore = manager.getDrawPile();

        boolean turnEnded = manager.playGambleCat();

        assertTrue(turnEnded);
        List<Card> discard = manager.getDiscardPile();
        assertEquals(CardType.Gamble_Cat, discard.get(discard.size() - 1).getTypeofCard());
        assertEquals("B", manager.getCurrentPlayer().getName());
        assertEquals(pileBefore, manager.getDrawPile());
    }

    @Test
    void playGambleCat_fourPlayersLucky_nextPlayerIsB() {
        GameManager manager = new GameManager(alwaysFirst());
        manager.startNewGame(List.of("A", "B", "C", "D"));
        manager.getCurrentPlayer().addCard(new Card(CardType.Gamble_Cat));

        assertTrue(manager.playGambleCat());
        assertEquals("B", manager.getCurrentPlayer().getName());
    }

    @Test
    void playGambleCat_fourPlayersUnlucky_turnContinues() {
        GameManager manager = new GameManager(alwaysLast());
        manager.startNewGame(List.of("A", "B", "C", "D"));
        manager.getCurrentPlayer().addCard(new Card(CardType.Gamble_Cat));
        final List<Card> pileBefore = manager.getDrawPile();

        boolean turnEnded = manager.playGambleCat();

        assertFalse(turnEnded);
        assertEquals("D", manager.getCurrentPlayer().getName());
        assertEquals(1, manager.getTurnsRemaining());
        assertEquals(pileBefore, manager.getDrawPile());
    }

    @Test
    void playGambleCat_unlucky_turnEndsAfterSecondDraw() {
        GameManager manager = new GameManager(alwaysLast());
        manager.startNewGame(List.of("A", "B", "C", "D"));
        manager.getCurrentPlayer().addCard(new Card(CardType.Gamble_Cat));
        manager.playGambleCat();
        manager.placeOnTopOfDrawPile(new Card(CardType.See_The_Future));
        manager.placeOnTopOfDrawPile(new Card(CardType.See_The_Future));

        manager.drawCard();
        assertEquals("D", manager.getCurrentPlayer().getName());

        manager.drawCard();
        assertEquals("A", manager.getCurrentPlayer().getName());
    }

    @Test
    void playGambleCat_unluckyMidDraw_cannotPlayCards() {
        GameManager manager = new GameManager(alwaysLast());
        manager.startNewGame(List.of("A", "B"));
        Player b = manager.getCurrentPlayer();
        b.addCard(new Card(CardType.Gamble_Cat));
        b.addCard(new Card(CardType.Attack));
        manager.playGambleCat();
        manager.placeOnTopOfDrawPile(new Card(CardType.See_The_Future));
        manager.drawCard();

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                manager::playAttack);
        assertEquals("gameManager.play.mustFinishDrawing", ex.getMessage());
        assertSame(b, manager.getCurrentPlayer());
        assertEquals(1, countType(b.getHand(), CardType.Attack));
    }

    @Test
    void playGambleCat_unluckyKittenDefused_countsAsOneDraw() {
        GameManager manager = new GameManager(alwaysLast());
        manager.startNewGame(List.of("A", "B"));
        manager.getCurrentPlayer().addCard(new Card(CardType.Gamble_Cat));
        manager.playGambleCat();
        manager.placeOnTopOfDrawPile(new Card(CardType.See_The_Future));
        manager.placeOnTopOfDrawPile(new Card(CardType.Exploding_Kitten));

        manager.drawCard();
        manager.playDefuse(manager.getDrawPile().size());
        assertEquals("B", manager.getCurrentPlayer().getName());

        manager.drawCard();
        assertEquals("A", manager.getCurrentPlayer().getName());
    }

    @Test
    void playGambleCat_luckyWhileUnderAttack_cancelsOneTurn() {
        GameManager manager = new GameManager(alwaysFirst());
        manager.startNewGame(List.of("A", "B"));
        manager.getCurrentPlayer().addCard(new Card(CardType.Attack));
        manager.playAttack();                                   // B, owes 2
        manager.getCurrentPlayer().addCard(new Card(CardType.Gamble_Cat));

        assertTrue(manager.playGambleCat());
        assertEquals("B", manager.getCurrentPlayer().getName());
        assertEquals(1, manager.getTurnsRemaining());
    }

    @Test
    void playGambleCat_unluckyWhileUnderAttack_twoDrawsThenOneTurnLeft() {
        GameManager manager = new GameManager(alwaysLast());
        manager.startNewGame(List.of("A", "B"));
        manager.getCurrentPlayer().addCard(new Card(CardType.Attack));
        manager.playAttack();                                   // A, owes 2
        manager.getCurrentPlayer().addCard(new Card(CardType.Gamble_Cat));
        manager.playGambleCat();
        manager.placeOnTopOfDrawPile(new Card(CardType.See_The_Future));
        manager.placeOnTopOfDrawPile(new Card(CardType.See_The_Future));

        manager.drawCard();
        manager.drawCard();

        assertEquals("A", manager.getCurrentPlayer().getName());
        assertEquals(1, manager.getTurnsRemaining());
    }

    @Test
    void playGambleCat_unluckyThenAttackBeforeDrawing_extraDrawCancelled() {
        GameManager manager = new GameManager(alwaysLast());
        manager.startNewGame(List.of("A", "B"));
        Player b = manager.getCurrentPlayer();
        b.addCard(new Card(CardType.Gamble_Cat));
        b.addCard(new Card(CardType.Attack));
        manager.playGambleCat();

        manager.playAttack();

        assertEquals("A", manager.getCurrentPlayer().getName());
        assertEquals(2, manager.getTurnsRemaining());
        manager.placeOnTopOfDrawPile(new Card(CardType.See_The_Future));
        manager.drawCard();
        assertEquals(1, manager.getTurnsRemaining());
    }

    @Test
    void playGambleCat_twoGambleCatsInHand_removesOnlyOne() {
        GameManager manager = new GameManager(alwaysFirst());
        manager.startNewGame(List.of("A", "B", "C"));
        Player a = manager.getCurrentPlayer();
        a.addCard(new Card(CardType.Gamble_Cat));
        a.addCard(new Card(CardType.Gamble_Cat));

        manager.playGambleCat();

        assertEquals(1, countType(a.getHand(), CardType.Gamble_Cat));
        assertEquals(1, countType(manager.getDiscardPile(), CardType.Gamble_Cat));
    }

    @Test
    void playGambleCat_unlucky_asksRandomForOneCoinFlip() {
        Random random = EasyMock.createNiceMock(Random.class);
        EasyMock.replay(random);
        GameManager manager = new GameManager(random);
        manager.startNewGame(List.of("A", "B"));
        manager.getCurrentPlayer().addCard(new Card(CardType.Gamble_Cat));

        EasyMock.resetToStrict(random);
        EasyMock.expect(random.nextInt(2)).andReturn(1);
        EasyMock.replay(random);

        boolean turnEnded = manager.playGambleCat();

        assertFalse(turnEnded);
        assertEquals("A", manager.getCurrentPlayer().getName());
        EasyMock.verify(random);
    }

    @Test
    void playGambleCat_lucky_asksRandomForOneCoinFlip() {
        Random random = EasyMock.createNiceMock(Random.class);
        EasyMock.replay(random);
        GameManager manager = new GameManager(random);
        manager.startNewGame(List.of("A", "B"));
        manager.getCurrentPlayer().addCard(new Card(CardType.Gamble_Cat));

        EasyMock.resetToStrict(random);
        EasyMock.expect(random.nextInt(2)).andReturn(0);
        EasyMock.replay(random);

        boolean turnEnded = manager.playGambleCat();

        assertTrue(turnEnded);
        assertEquals("B", manager.getCurrentPlayer().getName());
        EasyMock.verify(random);
    }

    @Test
    void playGambleCat_unluckyKittenOnSecondDraw_canStillDefuse() {
        GameManager manager = new GameManager(alwaysLast());
        manager.startNewGame(List.of("A", "B"));
        manager.getCurrentPlayer().addCard(new Card(CardType.Gamble_Cat));
        manager.playGambleCat();
        Card kitten = new Card(CardType.Exploding_Kitten);
        manager.placeOnTopOfDrawPile(kitten);
        manager.placeOnTopOfDrawPile(new Card(CardType.See_The_Future));

        manager.drawCard();                                   // first draw, now mid-draw
        manager.drawCard();                                   // second draw: the Kitten
        manager.playDefuse(manager.getDrawPile().size());

        List<Card> pile = manager.getDrawPile();
        assertSame(kitten, pile.get(pile.size() - 1));
        assertEquals("A", manager.getCurrentPlayer().getName());
    }
}