package domain;

import java.util.Random;

/**
 * Test helpers for constructing a {@link GameManager} with a controllable {@link Random}.
 */
public final class GameManagerFixtures {

    private GameManagerFixtures() {
    }

    /**
     * Returns a manager whose first-player pick always chooses index 0.
     *
     * @return a new game manager
     */
    public static GameManager alwaysFirstPlayer() {
        return new GameManager(new Random() {
            @Override
            public int nextInt(int bound) {
                return 0;
            }
        });
    }

    /**
     * Puts a card on top of the draw pile.
     *
     * @param manager the game under test
     * @param card the card to place
     */
    public static void putOnTop(GameManager manager, Card card) {
        manager.placeOnTopOfDrawPile(card);
    }

    /**
     * Returns a manager whose random choices always pick the last option: the last seat starts
     * and Gamble Cat comes up unlucky.
     *
     * @return a new game manager
     */
    public static GameManager alwaysLastPlayer() {
        return new GameManager(new Random() {
            @Override
            public int nextInt(int bound) {
                return bound - 1;
            }
        });
    }
}
