package domain;

import java.util.Random;

public final class GameManagerFixtures {

    private GameManagerFixtures() {
    }

    public static GameManager alwaysFirstPlayer() {
        return new GameManager(new Random() {
            @Override
            public int nextInt(int bound) {
                return 0;
            }
        });
    }

    public static void putOnTop(GameManager manager, Card card) {
        manager.placeOnTopOfDrawPile(card);
    }
}
