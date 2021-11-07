package model.players;

import model.SoccerBall;

import java.awt.*;

/**
 * A player in the MiniSoccerApp application.
 */
public abstract class GamePlayer implements Comparable<GamePlayer> {

	protected final String playerName;

	protected final Color playerColor;

	protected Point playerPosition;

	protected final PlayerStatistics playerStatistics;

	public GamePlayer(String name, Color color) {
		playerName = name;
		playerColor = color;
		playerStatistics = new PlayerStatistics();
		setInitialPosition();
	}

	/**
	 * @return True if the player is in possession of the ball, false otherwise.
	 */
	public boolean isPlayerHasBall() {
		Point playerPositionCenter = new Point(getPlayerPosition().x + 15, getPlayerPosition().y + 30);
		return playerPositionCenter.distance(SoccerBall.getSoccerBall().getPosition()) < 55;
	}

	/**
	 * The player takes possession of the ball.
	 */
	public void grabsBall() {
		SoccerBall ball = SoccerBall.getSoccerBall();
		if (getPlayerPosition().x + 15 > ball.getPosition().x) {
			ball.setPosition(new Point(getPlayerPosition().x - 10, getPlayerPosition().y + 55));
		} else {
			ball.setPosition(new Point(getPlayerPosition().x + 20, getPlayerPosition().y + 55));
		}
	}

	/**
	 * Moves the player to the left on the screen.
	 */
	public abstract void moveLeft();

	/**
	 * Moves the player to the right on the screen.
	 */
	public abstract void moveRight();

	/**
	 * Moves the player upwards on the screen.
	 */
	public abstract void moveUp();

	/**
	 * Moves the player downwards on the screen.
	 */
	public abstract void moveDown();

	/**
	 * Shoots the ball away from the player.
	 */
	public abstract void shootBall();

	/**
	 * @return The name of the player.
	 */
	public String getPlayerName() {
		return playerName;
	}

	/**
	 * @return The colour of the player's uniform.
	 */
	public Color getPlayerColor() {
		return playerColor;
	}

	/**
	 * @return The position of the player on the screen.
	 */
	public Point getPlayerPosition() {
		return playerPosition;
	}

	/**
	 * Places the player at their initial starting position on the screen.
	 */
	public abstract void setInitialPosition();

	/**
	 * Moves the player instantly to a new position on screen.
	 * @param newPosition - The player's new position on screen.
	 */
	public void setPlayerPosition(Point newPosition) {
		playerPosition = newPosition;
		if (isPlayerHasBall()) {
			grabsBall();
		}
	}

	/**
	 * @return The player's statistics.
	 */
	public Integer getPlayerStatistics() {
		return playerStatistics.getStatistics();
	}

	/**
	 * Sets the player's statistics to a new value.
	 * @param newStatistics - The new value for the player's statistics.
	 */
	public void setPlayerStatistics(Integer newStatistics) {
		playerStatistics.setStatistics(newStatistics);
	}

	@Override
	public int compareTo(GamePlayer otherPlayer) {
		return otherPlayer.getPlayerStatistics().compareTo(this.getPlayerStatistics());
	}

	@Override
	public abstract String toString();
}
