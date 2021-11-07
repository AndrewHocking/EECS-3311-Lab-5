package model;

import java.util.Timer;
import java.util.TimerTask;

import model.players.*;

/**
 * The soccer game that is taking place in the MiniSoccerApp application.
 */
public class SoccerGame {

	private Integer timeRemaining;

	private Integer goal;

	private Boolean isPaused;

	private Boolean isOver;

	private final PlayerCollection gamePlayers;

	public SoccerGame() {
		timeRemaining = 60;
		goal = 0;
		isPaused = false;
		isOver = false;
		SoccerBall.getSoccerBall().resetSoccerBall();
		PlayerFactory playerFactory = new PlayerFactory();
		gamePlayers = new PlayerCollection();
		gamePlayers.add(playerFactory.getPlayer("striker"));
		gamePlayers.add(playerFactory.getPlayer("goalkeeper"));
		startGame();
	}

	/**
	 * Starts the timer and runs the game, updating the goals and saves as they
	 * happen. When the time runs out, this method stops the game.
	 */
	private void startGame() {
		Timer timer = new Timer();
		TimerTask timerTask = new TimerTask() {
			@Override
			public void run() {
				if (!isPaused()) {
					if (getTimeRemaining() <= 0) {
						setOver(true);
						timer.cancel();
					} else {
						setTimeRemaining(getTimeRemaining() - 1);
					}
					if (isScored()) {
						setPaused(true);
						setGoal(getGoal() + 1);
						getActivePlayer().setPlayerStatistics(getActivePlayer().getPlayerStatistics() + 1);
						getGamePlayers().get("Striker").setInitialPosition();
						SoccerBall.getSoccerBall().resetSoccerBall();
					} else {
						automateGoalkeeper();
					}
				}
			}
		};
		timer.schedule(timerTask, 1000, 1000);
	}

	/**
	 * @return The amount of time remaining in the game.
	 */
	public Integer getTimeRemaining() {
		return timeRemaining;
	}

	/**
	 * Sets the amount of time remaining in the game, in seconds.
	 * 
	 * @param timeRemaining - The new amount of time remaining in the game, in
	 *                      seconds.
	 */
	public void setTimeRemaining(Integer timeRemaining) {
		this.timeRemaining = timeRemaining;
	}

	/**
	 * @return The number of goals scored.
	 */
	public Integer getGoal() {
		return goal;
	}

	/**
	 * Sets the number of goals scored.
	 * 
	 * @param newGoal - The new number of goals scored.
	 */
	public void setGoal(Integer newGoal) {
		goal = newGoal;
	}

	/**
	 * @return True if the game is paused, false otherwise.
	 */
	public Boolean isPaused() {
		return isPaused;
	}

	/**
	 * Sets the pause state of the game.
	 * 
	 * @param paused - True to pause the game, false otherwise.
	 */
	public void setPaused(Boolean paused) {
		isPaused = paused;
	}

	/**
	 * @return True if the game is over, false otherwise.
	 */
	public Boolean isOver() {
		return isOver;
	}

	/**
	 * Sets whether the game is over or not.
	 * 
	 * @param over - True if the game is over, false otherwise.
	 */
	public void setOver(Boolean over) {
		isOver = over;
	}

	/**
	 * @return The collection of players in this game.
	 */
	public PlayerCollection getGamePlayers() {
		return gamePlayers;
	}

	/**
	 * Controls the goalkeeper. If the ball is on the goalkeeper's side, the
	 * goalkeeper will shoot the ball out. Otherwise, they will move randomly around
	 * the net.
	 */
	public void automateGoalkeeper() {
		SoccerBall soccerBall = SoccerBall.getSoccerBall();
		Goalkeeper goalkeeper = (Goalkeeper) gamePlayers.get("Goalkeeper");
		if (soccerBall.onGoalkeeperSide()) {
			goalkeeper.grabsBall();
			goalkeeper.shootBall();
			goalkeeper.setPlayerStatistics(goalkeeper.getPlayerStatistics() + 1);
		} else {
			goalkeeper.moveRandomly();
		}
	}

	/**
	 * @return True if the ball is in the gate, false otherwise.
	 */
	public boolean isScored() {
		return SoccerBall.getSoccerBall().inGate();
	}

	/**
	 * @return The active Striker player.
	 */
	public GamePlayer getActivePlayer() {
		return gamePlayers.get("Striker");
	}
}
