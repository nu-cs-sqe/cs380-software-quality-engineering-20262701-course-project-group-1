package domain;

import java.util.Random;

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
    private boolean extraDraw = false;
    private boolean midDraw = false;

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

    public void attackNextPlayer() {
        requireStarted();
        int transferred = turnsToTransfer();
        advanceToNextPlayer();
        turnsRemaining = transferred + TURNS_FROM_ATTACK;
        underAttack = true;
    }

    public int getTurnsRemaining() {
        requireStarted();
        return turnsRemaining;
    }

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
