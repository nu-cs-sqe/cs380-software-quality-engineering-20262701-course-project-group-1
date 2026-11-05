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
    }

    public int getCurrentIndex() {
        requireStarted();
        return currentIndex;
    }

    public void attackNextPlayer() {
        requireStarted();
        advanceToNextPlayer();
        turnsRemaining = TURNS_FROM_ATTACK;
    }

    public int getTurnsRemaining() {
        requireStarted();
        return turnsRemaining;
    }

    public void endTurn() {
        requireStarted();
        turnsRemaining--;
        if (turnsRemaining == 0) {
            advanceToNextPlayer();
            turnsRemaining = NORMAL_TURNS;
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
}