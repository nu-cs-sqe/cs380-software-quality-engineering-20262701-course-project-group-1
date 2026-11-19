package domain;

import java.util.Random;

/**
 * Tracks whose turn it is and how many turns the current player still owes.
 */
public class TurnManager {

    private static final String PLAYER_COUNT_OUT_OF_RANGE = "turnManager.playerCount.outOfRange";
    private static final String RANDOM_NULL = "turnManager.random.null";
    private static final String NOT_STARTED = "turnManager.notStarted";
    private static final int NOT_STARTED_INDEX = -1;
    private static final int TURNS_FROM_ATTACK = 2;
    private static final int NORMAL_TURNS = 1;
    private int turnsRemaining = NORMAL_TURNS;
    private int playerCount;
    private int currentIndex = NOT_STARTED_INDEX;
    private boolean underAttack = false;
    private boolean extraDraw = false;
    private boolean midDraw = false;

    /**
     * Starts turn tracking with a randomly chosen first player.
     *
     * @param playerCount the number of players, from 2 to 4
     * @param random the source used to pick the first player
     * @throws IllegalArgumentException if {@code playerCount} is out of range or
     *     {@code random} is null
     */
    public void pickStart(int playerCount, Random random) {
        if (playerCount < GameRules.MIN_PLAYERS || playerCount > GameRules.MAX_PLAYERS) {
            throw new IllegalArgumentException(PLAYER_COUNT_OUT_OF_RANGE);
        }
        if (random == null) {
            throw new IllegalArgumentException(RANDOM_NULL);
        }
        this.playerCount = playerCount;
        currentIndex = random.nextInt(playerCount);
        turnsRemaining = NORMAL_TURNS;
        underAttack = false;
        extraDraw = false;
        midDraw = false;
    }

    /**
     * Returns the seat index of the current player.
     *
     * @return the current player's index
     * @throws IllegalStateException if {@link #pickStart} has not been called
     */
    public int getCurrentIndex() {
        requireStarted();
        return currentIndex;
    }

    /**
     * Ends the current player's turns and makes the next player take two turns.
     *
     * <p>If the current player is under attack, their remaining turns are passed on as well.
     *
     * @throws IllegalStateException if {@link #pickStart} has not been called
     */
    public void attackNextPlayer() {
        requireStarted();
        int transferred = turnsToTransfer();
        advanceToNextPlayer();
        turnsRemaining = transferred + TURNS_FROM_ATTACK;
        underAttack = true;
    }

    /**
     * Returns how many turns the current player still owes.
     *
     * @return the number of turns remaining
     * @throws IllegalStateException if {@link #pickStart} has not been called
     */
    public int getTurnsRemaining() {
        requireStarted();
        return turnsRemaining;
    }

    /**
     * Uses up one of the current player's turns, moving to the next player when none are left.
     *
     * <p>If an extra draw is owed, the first call only marks mid-draw and does not advance.
     *
     * @throws IllegalStateException if {@link #pickStart} has not been called
     */
    public void endTurn() {
        requireStarted();
        if (extraDraw) {
            extraDraw = false;
            midDraw = true;
            return;
        }
        midDraw = false;
        turnsRemaining--;
        if (turnsRemaining == 0) {
            advanceToNextPlayer();
            turnsRemaining = NORMAL_TURNS;
            underAttack = false;
        }
    }

    /**
     * Makes the current turn end only after one more draw than usual.
     *
     * @throws IllegalStateException if {@link #pickStart} has not been called
     */
    public void addExtraDraw() {
        requireStarted();
        extraDraw = true;
    }

    /**
     * Returns whether the current player has started drawing but still owes another draw.
     *
     * @return true if the player is between the two draws of a double-draw turn
     * @throws IllegalStateException if {@link #pickStart} has not been called
     */
    public boolean isMidDraw() {
        requireStarted();
        return midDraw;
    }

    /**
     * Removes the current player from the turn order and passes the turn to the next living player.
     *
     * <p>Any turns the eliminated player still owed end with them, so the next player owes one
     * turn and is not under attack. Eliminated seats are skipped by every later turn change.
     *
     * @throws IllegalStateException if {@link #pickStart} has not been called
     */
    public void eliminateCurrentPlayer() {
        requireStarted();
        turnsRemaining = NORMAL_TURNS;
        underAttack = false;
        advanceToNextPlayer();
    }

    private void requireStarted() {
        if (currentIndex == NOT_STARTED_INDEX) {
            throw new IllegalStateException(NOT_STARTED);
        }
    }

    private void advanceToNextPlayer() {
        currentIndex = (currentIndex + 1) % playerCount;
        extraDraw = false;
        midDraw = false;
    }

    void advanceToNextSeat() {
        requireStarted();
        advanceToNextPlayer();
    }

    void resetTurnsToNormal() {
        requireStarted();
        turnsRemaining = NORMAL_TURNS;
        underAttack = false;
    }

    private int turnsToTransfer() {
        return underAttack ? turnsRemaining : 0;
    }
}
