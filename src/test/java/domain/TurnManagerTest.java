package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    void attackNextPlayer_beforePickStart_throwsIllegalStateException() {
        TurnManager turnManager = new TurnManager();

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                turnManager::attackNextPlayer);
        assertEquals("turnManager.notStarted", ex.getMessage());
    }

    @Test
    void attackNextPlayer_twoPlayersIndexZero_nextPlayerOwesTwo() {
        TurnManager turnManager = new TurnManager();
        turnManager.pickStart(2, alwaysFirst());

        turnManager.attackNextPlayer();

        assertEquals(1, turnManager.getCurrentIndex());
        assertEquals(2, turnManager.getTurnsRemaining());
    }

    @Test
    void attackNextPlayer_fourPlayersLastSeat_wrapsToIndexZeroOwesTwo() {
        TurnManager turnManager = new TurnManager();
        turnManager.pickStart(4, alwaysLast());   // index 3

        turnManager.attackNextPlayer();

        assertEquals(0, turnManager.getCurrentIndex());
        assertEquals(2, turnManager.getTurnsRemaining());
    }

    @Test
    void attackNextPlayer_whileOwingTwo_stacksToFour() {
        TurnManager turnManager = new TurnManager();
        turnManager.pickStart(2, alwaysFirst());
        turnManager.attackNextPlayer();   // index 1, owes 2

        turnManager.attackNextPlayer();

        assertEquals(0, turnManager.getCurrentIndex());
        assertEquals(4, turnManager.getTurnsRemaining());
    }

    @Test
    void attackNextPlayer_lastTurnOfAttack_nextPlayerOwesThree() {
        TurnManager turnManager = new TurnManager();
        turnManager.pickStart(2, alwaysFirst());
        turnManager.attackNextPlayer();   // index 1, owes 2
        turnManager.endTurn();            // index 1, owes 1

        turnManager.attackNextPlayer();

        assertEquals(0, turnManager.getCurrentIndex());
        assertEquals(3, turnManager.getTurnsRemaining());
    }

    @Test
    void attackNextPlayer_afterAttackFullyServed_nextPlayerOwesTwo() {
        TurnManager turnManager = new TurnManager();
        turnManager.pickStart(2, alwaysFirst());
        turnManager.attackNextPlayer();   // index 1, owes 2
        turnManager.endTurn();            // index 1, owes 1
        turnManager.endTurn();            // index 0, owes 1, attack over

        turnManager.attackNextPlayer();

        assertEquals(1, turnManager.getCurrentIndex());
        assertEquals(2, turnManager.getTurnsRemaining());
    }

    @Test
    void pickStart_afterAttack_nextAttackOwesTwo() {
        TurnManager turnManager = new TurnManager();
        turnManager.pickStart(2, alwaysFirst());
        turnManager.attackNextPlayer();   // under attack

        turnManager.pickStart(2, alwaysFirst());
        turnManager.attackNextPlayer();

        assertEquals(1, turnManager.getCurrentIndex());
        assertEquals(2, turnManager.getTurnsRemaining());
    }

    @Test
    void addExtraDraw_beforePickStart_throwsIllegalStateException() {
        TurnManager turnManager = new TurnManager();

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                turnManager::addExtraDraw);
        assertEquals("turnManager.notStarted", ex.getMessage());
    }

    @Test
    void addExtraDraw_thenOneEndTurn_samePlayerMidDraw() {
        TurnManager turnManager = new TurnManager();
        turnManager.pickStart(2, alwaysFirst());
        turnManager.addExtraDraw();

        turnManager.endTurn();

        assertEquals(0, turnManager.getCurrentIndex());
        assertEquals(1, turnManager.getTurnsRemaining());
        assertTrue(turnManager.isMidDraw());
    }

    @Test
    void addExtraDraw_thenTwoEndTurns_advancesAndNotMidDraw() {
        TurnManager turnManager = new TurnManager();
        turnManager.pickStart(2, alwaysFirst());
        turnManager.addExtraDraw();

        turnManager.endTurn();
        turnManager.endTurn();

        assertEquals(1, turnManager.getCurrentIndex());
        assertEquals(1, turnManager.getTurnsRemaining());
        assertFalse(turnManager.isMidDraw());
    }

    @Test
    void addExtraDraw_fourPlayersLastSeat_wrapsAfterTwoEndTurns() {
        TurnManager turnManager = new TurnManager();
        turnManager.pickStart(4, alwaysLast());
        turnManager.addExtraDraw();

        turnManager.endTurn();
        turnManager.endTurn();

        assertEquals(0, turnManager.getCurrentIndex());
        assertFalse(turnManager.isMidDraw());
    }

    @Test
    void addExtraDraw_whileOwingTwo_firstTurnUsesTwoDraws() {
        TurnManager turnManager = new TurnManager();
        turnManager.pickStart(2, alwaysFirst());
        turnManager.attackNextPlayer();   // index 1, owes 2
        turnManager.addExtraDraw();

        turnManager.endTurn();
        turnManager.endTurn();

        assertEquals(1, turnManager.getCurrentIndex());
        assertEquals(1, turnManager.getTurnsRemaining());
    }

    @Test
    void addExtraDraw_calledTwice_doesNotStack() {
        TurnManager turnManager = new TurnManager();
        turnManager.pickStart(2, alwaysFirst());
        turnManager.addExtraDraw();
        turnManager.addExtraDraw();

        turnManager.endTurn();
        turnManager.endTurn();

        assertEquals(1, turnManager.getCurrentIndex());
    }

    @Test
    void addExtraDraw_thenAttack_extraDrawNotPassedOn() {
        TurnManager turnManager = new TurnManager();
        turnManager.pickStart(2, alwaysFirst());
        turnManager.addExtraDraw();
        turnManager.attackNextPlayer();   // index 1, owes 2

        turnManager.endTurn();

        assertEquals(1, turnManager.getCurrentIndex());
        assertEquals(1, turnManager.getTurnsRemaining());
    }

    @Test
    void isMidDraw_beforePickStart_throwsIllegalStateException() {
        TurnManager turnManager = new TurnManager();

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                turnManager::isMidDraw);
        assertEquals("turnManager.notStarted", ex.getMessage());
    }

    @Test
    void isMidDraw_afterPickStart_isFalse() {
        TurnManager turnManager = new TurnManager();
        turnManager.pickStart(2, alwaysFirst());

        assertFalse(turnManager.isMidDraw());
    }

    @Test
    void isMidDraw_afterAddExtraDrawBeforeDrawing_isFalse() {
        TurnManager turnManager = new TurnManager();
        turnManager.pickStart(2, alwaysFirst());
        turnManager.addExtraDraw();

        assertFalse(turnManager.isMidDraw());
    }

    @Test
    void pickStart_afterAddExtraDraw_clearsExtraDraw() {
        TurnManager turnManager = new TurnManager();
        turnManager.pickStart(2, alwaysFirst());
        turnManager.addExtraDraw();

        turnManager.pickStart(2, alwaysFirst());
        turnManager.endTurn();

        assertEquals(1, turnManager.getCurrentIndex());
    }

    @Test
    void eliminateCurrentPlayer_beforePickStart_throwsIllegalStateException() {
        TurnManager turnManager = new TurnManager();

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                turnManager::eliminateCurrentPlayer);
        assertEquals("turnManager.notStarted", ex.getMessage());
    }

    @Test
    void eliminateCurrentPlayer_owingOne_nextPlayerTakesTurn() {
        TurnManager turnManager = new TurnManager();
        turnManager.pickStart(3, alwaysFirst());

        turnManager.eliminateCurrentPlayer();

        assertEquals(1, turnManager.getCurrentIndex());
        assertEquals(1, turnManager.getTurnsRemaining());
    }

    @Test
    void eliminateCurrentPlayer_whileOwingTwo_nextPlayerOwesOne() {
        TurnManager turnManager = new TurnManager();
        turnManager.pickStart(3, alwaysFirst());
        turnManager.attackNextPlayer();   // index 1, owes 2

        turnManager.eliminateCurrentPlayer();

        assertEquals(2, turnManager.getCurrentIndex());
        assertEquals(1, turnManager.getTurnsRemaining());
    }
}