package domain;

import java.util.ArrayList;
import java.util.List;

public class Player {

	private static final String CARD_NOT_IN_HAND = "player.card.notInHand";
	private final String name;
	private final List<Card> hand = new ArrayList<>();

	public Player(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public boolean isAlive() {
		return true;
	}

	public void addCard(Card card) {
		hand.add(card);
	}

	public List<Card> getHand() {
		return new ArrayList<>(hand);
	}

	public Card removeCardOfType(CardType type) {
		for (int i = 0; i < hand.size(); i++) {
			if (hand.get(i).getTypeofCard() == type) {
				return hand.remove(i);
			}
		}
		throw new IllegalStateException(CARD_NOT_IN_HAND);
	}
}
