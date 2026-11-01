package domain;

import java.util.ArrayList;
import java.util.List;

public class Player {

	private final String name;

	public Player(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public boolean isAlive() {
		return true;
	}

	public List<Card> getHand() {
		return new ArrayList<>();
	}
}
