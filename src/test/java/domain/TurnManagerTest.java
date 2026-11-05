package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Random;
import org.junit.jupiter.api.Test;

class TurnManagerTest {

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
    void pickStart_twoPlayers_randomFirst_indexIsZero() {
        TurnManager turnManager = new TurnManager();

        turnManager.pickStart(2, alwaysFirst());

        assertEquals(0, turnManager.getCurrentIndex());
    }

    @Test
    void pickStart_twoPlayers_randomLast_indexIsOne() {
        TurnManager turnManager = new TurnManager();

        turnManager.pickStart(2, alwaysLast());

        assertEquals(1, turnManager.getCurrentIndex());
    }

    @Test
    void pickStart_fourPlayers_randomFirst_indexIsZero() {
        TurnManager turnManager = new TurnManager();

        turnManager.pickStart(4, alwaysFirst());

        assertEquals(0, turnManager.getCurrentIndex());
    }

    @Test
    void pickStart_fourPlayers_randomLast_indexIsThree() {
        TurnManager turnManager = new TurnManager();

        turnManager.pickStart(4, alwaysLast());

        assertEquals(3, turnManager.getCurrentIndex());
    }

    @Test
    void pickStart_onePlayer_throwsIllegalArgumentException() {
        TurnManager turnManager = new TurnManager();

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> turnManager.pickStart(1, alwaysFirst()));
        assertEquals("turnManager.playerCount.outOfRange", ex.getMessage());
    }

    @Test
    void pickStart_fivePlayers_throwsIllegalArgumentException() {
        TurnManager turnManager = new TurnManager();

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> turnManager.pickStart(5, alwaysFirst()));
        assertEquals("turnManager.playerCount.outOfRange", ex.getMessage());
    }

    @Test
    void pickStart_zeroPlayers_throwsIllegalArgumentException() {
        TurnManager turnManager = new TurnManager();

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> turnManager.pickStart(0, alwaysFirst()));
        assertEquals("turnManager.playerCount.outOfRange", ex.getMessage());
    }

    @Test
    void pickStart_nullRandom_throwsIllegalArgumentException() {
        TurnManager turnManager = new TurnManager();

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> turnManager.pickStart(2, null));
        assertEquals("turnManager.random.null", ex.getMessage());
    }

    @Test
    void pickStart_calledAgain_replacesPreviousStart() {
        TurnManager turnManager = new TurnManager();
        turnManager.pickStart(4, alwaysLast());

        turnManager.pickStart(2, alwaysFirst());

        assertEquals(0, turnManager.getCurrentIndex());
    }

    @Test
    void getCurrentIndex_beforePickStart_throwsIllegalStateException() {
        TurnManager turnManager = new TurnManager();

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                turnManager::getCurrentIndex);
        assertEquals("turnManager.notStarted", ex.getMessage());
    }

    @Test
    void getCurrentIndex_afterRejectedPickStart_throwsIllegalStateException() {
        TurnManager turnManager = new TurnManager();
        assertThrows(IllegalArgumentException.class,
                () -> turnManager.pickStart(1, alwaysFirst()));

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                turnManager::getCurrentIndex);
        assertEquals("turnManager.notStarted", ex.getMessage());
    }

    @Test
    void pickStart_afterAttack_resetsTurnsRemainingToOne() {
        TurnManager turnManager = new TurnManager();
        turnManager.pickStart(2, alwaysFirst());
        turnManager.attackNextPlayer();

        turnManager.pickStart(2, alwaysFirst());

        assertEquals(1, turnManager.getTurnsRemaining());
    }

    @Test
    void getTurnsRemaining_beforePickStart_throwsIllegalStateException() {
        TurnManager turnManager = new TurnManager();

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                turnManager::getTurnsRemaining);
        assertEquals("turnManager.notStarted", ex.getMessage());
    }

    @Test
    void getTurnsRemaining_afterPickStart_isOne() {
        TurnManager turnManager = new TurnManager();

        turnManager.pickStart(2, alwaysFirst());

        assertEquals(1, turnManager.getTurnsRemaining());
    }

    @Test
    void endTurn_beforePickStart_throwsIllegalStateException() {
        TurnManager turnManager = new TurnManager();

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                turnManager::endTurn);
        assertEquals("turnManager.notStarted", ex.getMessage());
    }

    @Test
    void endTurn_twoPlayersIndexZero_advancesToIndexOne() {
        TurnManager turnManager = new TurnManager();
        turnManager.pickStart(2, alwaysFirst());

        turnManager.endTurn();

        assertEquals(1, turnManager.getCurrentIndex());
        assertEquals(1, turnManager.getTurnsRemaining());
    }

    @Test
    void endTurn_fourPlayersLastSeat_wrapsToIndexZero() {
        TurnManager turnManager = new TurnManager();
        turnManager.pickStart(4, alwaysLast());

        turnManager.endTurn();

        assertEquals(0, turnManager.getCurrentIndex());
        assertEquals(1, turnManager.getTurnsRemaining());
    }

    @Test
    void endTurn_owesTwo_samePlayerNowOwesOne() {
        TurnManager turnManager = new TurnManager();
        turnManager.pickStart(2, alwaysFirst());
        turnManager.attackNextPlayer();   // index 1, owes 2

        turnManager.endTurn();

        assertEquals(1, turnManager.getCurrentIndex());
        assertEquals(1, turnManager.getTurnsRemaining());
    }

    @Test
    void attackNextPlayer_twoPlayersIndexZero_nextPlayerOwesTwo() {
        TurnManager turnManager = new TurnManager();
        turnManager.pickStart(2, alwaysFirst());

        turnManager.attackNextPlayer();

        assertEquals(1, turnManager.getCurrentIndex());
        assertEquals(2, turnManager.getTurnsRemaining());
    }
}