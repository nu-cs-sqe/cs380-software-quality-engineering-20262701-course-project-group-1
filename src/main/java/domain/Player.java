package domain;

import java.util.ArrayList;
import java.util.List;

public class Player {

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
		return hand.remove(0);
	}
}
