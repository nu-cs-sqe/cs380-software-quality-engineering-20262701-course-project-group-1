package domain;

import java.util.Random;

/**
 * Tracks whose turn it is and how many turns the current player still owes.
 */
public class TurnManager {

    private static final String PLAYER_COUNT_OUT_OF_RANGE = "turnManager.playerCount.outOfRange";
    private static final String RANDOM_NULL = "turnManager.random.null";
    private static final String NOT_STARTED = "turnManager.notStarted";
    private static final int MIN_PLAYERS = 2;
    private static final int MAX_PLAYERS = 4;
    private static final int NOT_STARTED_INDEX = -1;
    private static final int TURNS_FROM_ATTACK = 2;
    private static final int NORMAL_TURNS = 1;
    private int turnsRemaining = NORMAL_TURNS;
    private int playerCount;
    private int currentIndex = NOT_STARTED_INDEX;
    private boolean underAttack = false;

    /**
     * Starts turn tracking with a randomly chosen first player.
     *
     * @param playerCount the number of players, from 2 to 4
     * @param random the source used to pick the first player
     * @throws IllegalArgumentException if {@code playerCount} is out of range or
     *     {@code random} is null
     */
    public void pickStart(int playerCount, Random random) {
        if (playerCount < MIN_PLAYERS || playerCount > MAX_PLAYERS) {
            throw new IllegalArgumentException(PLAYER_COUNT_OUT_OF_RANGE);
        }
        if (random == null) {
            throw new IllegalArgumentException(RANDOM_NULL);
        }
        this.playerCount = playerCount;
        currentIndex = random.nextInt(playerCount);
        turnsRemaining = NORMAL_TURNS;
        underAttack = false;
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
     * @throws IllegalStateException if {@link #pickStart} has not been called
     */
    public void endTurn() {
        requireStarted();
        turnsRemaining--;
        if (turnsRemaining == 0) {
            advanceToNextPlayer();
            turnsRemaining = NORMAL_TURNS;
            underAttack = false;
        }
    }

    private void requireStarted() {
        if (currentIndex == NOT_STARTED_INDEX) {
            throw new IllegalStateException(NOT_STARTED);
        }
    }

    private void advanceToNextPlayer() {
        currentIndex = (currentIndex + 1) % playerCount;
    }

    private int turnsToTransfer() {
        return underAttack ? turnsRemaining : 0;
    }
}
