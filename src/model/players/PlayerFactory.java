package model.players;

import java.awt.Color;

/**
 * A factory class for the GamePlayer subclasses (Striker, Goalkeeper).
 * @author Andrew Hocking
 */
public class PlayerFactory {
	
	private static final String STRIKER = "striker";
	private static final String GOALKEEPER = "goalkeeper";

	public GamePlayer getPlayer(String playerType) {
		switch(playerType.toLowerCase().trim()) {
		case STRIKER:
			return new Striker("Striker", Color.BLUE);
		case GOALKEEPER:
			return new Goalkeeper("Goalkeeper", Color.YELLOW);
		default:
			throw new IllegalArgumentException("Invalid player type.");
		}
	}

}
