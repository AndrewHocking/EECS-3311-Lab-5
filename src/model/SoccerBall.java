package model;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A SoccerBall in the MiniSoccerApp application.
 */
public class SoccerBall {

	private static final SoccerBall soccerBall = new SoccerBall();

	private Point position;

	private double velocity;

	private final Color color;

	private SoccerBall() {
		color = Color.white;
		resetSoccerBall();
	}

	/**
	 * @return The Singleton instance of the soccer ball.
	 */
	public static SoccerBall getSoccerBall() {
		return soccerBall;
	}

	/**
	 * Moves the soccer ball across the screen in a smoothly decelerating motion.
	 * @param initialDistance - The initial distance from the final location.
	 * @param initialVelocity - The initial velocity of the ball when motion begins.
	 * @param acceleration - The rate at which the velocity will decrease as the ball moves.
	 */
	public void moveBall(int initialDistance, double initialVelocity, double acceleration) {
		moveBallY(initialDistance);
		setVelocity(initialVelocity);
		Timer timer = new Timer();
		TimerTask repaintTask = new TimerTask() {
			@Override
			public void run() {
				if (Math.abs(velocity) < 2) {
					velocity = 0.0;
					timer.cancel();
				} else {
					velocity = velocity - acceleration;
				}
				moveBallY((int) velocity);
			}
		};
		timer.schedule(repaintTask, 0, 10);
	}

	/**
	 * Moves the ball vertically across the screen in a linear motion.
	 * @param distance - The distance to move the ball up the screen.
	 */
	public void moveBallY(int distance) {
		if (getPosition().y + distance < 510 && getPosition().y - distance > 20) {
			setPosition(new Point(getPosition().x, getPosition().y - distance));
		} else {
			setVelocity(0.0);
		}
	}

	/**
	 * Places the soccer ball at its default start position.
	 */
	public void resetSoccerBall() {
		setVelocity(0.0);
		setPosition(new Point(480, 500));
	}

	/**
	 * Checks if the ball is on the goalkeeper's side.
	 * @return True if the ball is on the goalkeeper's side of the screen, or false otherwise.
	 */
	public boolean onGoalkeeperSide() {
		return getPosition().y < 200;
	}

	/**
	 * Checks if the ball is in the goal.
	 * @return True if the ball is inside the goal, or false otherwise.
	 */
	public boolean inGate() {
		return getPosition().x > 180 && getPosition().x < 400 && getPosition().y > 10 && getPosition().y < 60;
	}

	/**
	 * Sets the velocity of the ball.
	 * @param velocity - The new velocity of the ball.
	 */
	public void setVelocity(double velocity) {
		this.velocity = velocity;
	}

	/**
	 * @return The position of the soccer ball on the screen.
	 */
	public Point getPosition() {
		return position;
	}

	/**
	 * Instantly moves the soccer ball to a position on screen.
	 * @param position - The new position for the soccer ball.
	 */
	public void setPosition(Point position) {
		this.position = position;
	}

	/**
	 * @return The colour of the soccer ball.
	 */
	public Color getColor() {
		return color;
	}
}
