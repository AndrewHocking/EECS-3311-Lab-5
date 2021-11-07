package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.NoSuchElementException;

import javax.swing.JFrame;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;

import controller.GameListener;
import controller.MenubarListener;
import model.players.GamePlayer;
import model.players.PlayerCollection;
import model.players.PlayerFactory;
import view.GameMenuBar;
import view.GamePanel;

@TestMethodOrder(OrderAnnotation.class)
class AppTests {
	
	private static Robot bot;
	private static JFrame gameFrame;
	private static GamePanel gamePanel;
	
	/**
	 * A method extremely similar to MiniSoccerApp.main, that launches the app but retains references
	 * to the JFrame and GamePanel for testing purposes.
	 * @throws AWTException
	 */
	@BeforeAll
	static void launchApp() throws AWTException {
		
		gameFrame = new JFrame("Mini Soccer");
		gamePanel = new GamePanel();
		gamePanel.setName("Game Panel");
		GameListener gameListener = new GameListener(gamePanel);
		MenubarListener menubarListener = new MenubarListener(gamePanel);
		GameMenuBar gameMenuBar = new GameMenuBar(menubarListener);
		gameMenuBar.setName("Menu Bar");
		gameFrame.add(gamePanel);
		gameFrame.addKeyListener(gameListener);
		gameFrame.setJMenuBar(gameMenuBar);
		gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameFrame.setSize(600, 600);
		gameFrame.setLocationRelativeTo(null);
		gameFrame.setResizable(false);
		gameFrame.setVisible(true);
		
		bot = new Robot();
		bot.delay(1000);
	}
	
	/**
	 * Starts a new game before each test begins.
	 * @throws AWTException
	 */
	@BeforeEach
	void newGame() throws AWTException {
		gamePanel.setupSoccerGame();
		Assert.assertTrue(gamePanel.getGame().getGoal() == 0);
	}
	
	/**
	 * Helper method to emulate pressing and holding a key on the keyboard by doing several repeated key presses. 
	 * @param keyCode
	 * @param duration
	 */
	private static void holdKeyAndRelease(int keyCode, long duration) {
		bot.setAutoDelay(50);
		long start = System.currentTimeMillis();
		while (System.currentTimeMillis() - start < duration) {
		    bot.keyPress(keyCode);
		}
		bot.keyRelease(keyCode);
	}

	/**
	 * Score a goal and check that the goal count increases.
	 * @throws InterruptedException
	 */
	@Order(1)
	@Test
	void scoreGoalTest() throws InterruptedException {
		// Shoot the ball, but not far enough to reach the goal.
		bot.keyPress(KeyEvent.VK_SPACE);
		bot.keyRelease(KeyEvent.VK_SPACE);
		assertEquals(0, gamePanel.getGame().getGoal().intValue());
		assertEquals(0, gamePanel.getGame().getActivePlayer().getPlayerStatistics().intValue());
		
		// Get the ball, move up to the goal, and score.
		holdKeyAndRelease(KeyEvent.VK_UP, 3000);
		holdKeyAndRelease(KeyEvent.VK_LEFT, 3000);
		bot.keyPress(KeyEvent.VK_SPACE);
		bot.keyRelease(KeyEvent.VK_SPACE);
		bot.delay(3000);
		assertEquals(1, gamePanel.getGame().getGoal().intValue());
		assertEquals(1, gamePanel.getGame().getActivePlayer().getPlayerStatistics().intValue());
	}
	
	/**
	 * Fail to score a goal twice, and check that the goal count does not increase.
	 */
	@Test
	void failGoalsTest() {
		// Shoot the ball directly into the goalkeeper.
		holdKeyAndRelease(KeyEvent.VK_UP, 3000);
		holdKeyAndRelease(KeyEvent.VK_LEFT, 1500);
		bot.keyPress(KeyEvent.VK_SPACE);
		bot.keyRelease(KeyEvent.VK_SPACE);
		bot.delay(3000);
		assertEquals(0, gamePanel.getGame().getGoal().intValue());
		assertEquals(0, gamePanel.getGame().getActivePlayer().getPlayerStatistics().intValue());
		
		// Go collect the ball and shoot it again, but miss the net.
		holdKeyAndRelease(KeyEvent.VK_DOWN, 3000);
		holdKeyAndRelease(KeyEvent.VK_LEFT, 2000);
		holdKeyAndRelease(KeyEvent.VK_RIGHT, 3000);
		holdKeyAndRelease(KeyEvent.VK_UP, 3000);
		bot.keyPress(KeyEvent.VK_SPACE);
		bot.keyRelease(KeyEvent.VK_SPACE);
		assertEquals(0, gamePanel.getGame().getGoal().intValue());
		assertEquals(0, gamePanel.getGame().getActivePlayer().getPlayerStatistics().intValue());
	}
	
	/**
	 * Pause the game, attempt to pause it again, and verify the error message appears. Do the same for resuming the game.
	 * Then wait for the game to time out, try to pause and resume the game, and verify the error messages appear.
	 * @throws Exception
	 */
	@Test
	void pauseResumeTest() throws Exception {
		// Capture the output to verify error messages
		final PrintStream originalOut = System.out;
		final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

		Exception ex = null;
		try {
		    System.setOut(new PrintStream(outContent));
			bot.delay(1000);
		    
			// Hit pause twice
			assertFalse(gamePanel.getGame().isPaused());
			bot.keyPress(KeyEvent.VK_P);
			bot.keyRelease(KeyEvent.VK_P);
			bot.delay(1000);
			assertTrue(gamePanel.getGame().isPaused());
			bot.keyPress(KeyEvent.VK_P);
			bot.keyRelease(KeyEvent.VK_P);
			bot.delay(1000);
			assertTrue(gamePanel.getGame().isPaused());
			bot.delay(1000);
			String output = "game is already on pause!";
			assertEquals(output, outContent.toString().trim());
			
			// Hit resume twice
			bot.keyPress(KeyEvent.VK_R);
			bot.keyRelease(KeyEvent.VK_R);
			bot.delay(1000);
			assertFalse(gamePanel.getGame().isPaused());
			bot.keyPress(KeyEvent.VK_R);
			bot.keyRelease(KeyEvent.VK_R);
			bot.delay(1000);
			assertFalse(gamePanel.getGame().isPaused());
			bot.delay(1000);
			output += "\ngame is already running!";
			assertEquals(output, outContent.toString().trim());

			assertFalse(gamePanel.getGame().isOver());
			// Wait until game ends
			bot.delay(60000);
			bot.delay(5000);
			assertFalse(gamePanel.getGame().isPaused());
			assertTrue(gamePanel.getGame().isOver());
			
			// Hit pause
			bot.keyPress(KeyEvent.VK_P);
			bot.keyRelease(KeyEvent.VK_P);
			bot.delay(1000);
			output += "\ngame is over, please start a new game.";
			assertTrue(gamePanel.getGame().isOver());
			assertFalse(gamePanel.getGame().isPaused());
			assertEquals(output, outContent.toString().trim());

			// Hit resume
			bot.keyPress(KeyEvent.VK_R);
			bot.keyRelease(KeyEvent.VK_R);
			bot.delay(1000);
			output += "\ngame is over, please start a new game.";
			assertTrue(gamePanel.getGame().isOver());
			assertFalse(gamePanel.getGame().isPaused());
			assertEquals(output, outContent.toString().trim());

		} catch (Exception e) {
			ex = e;
		} finally {
			// Reset the output
			System.setOut(originalOut);
			if (ex != null) {
			    throw ex;
			}
		}
	}
	
	/**
	 * Start a new game and check that the timer and both players' statistics have been reset.
	 */
	@Test
	void newGameTest() {
		gamePanel.getGame().setGoal(3);
		assertEquals(3, gamePanel.getGame().getGoal().intValue());
		for (GamePlayer player : gamePanel.getGame().getGamePlayers()) {
			player.setPlayerStatistics(3);
			assertEquals(3, player.getPlayerStatistics().intValue());
		}
		bot.delay(11000);
		assertTrue(gamePanel.getGame().getTimeRemaining().intValue() <= 50);
		
		bot.keyPress(KeyEvent.VK_N);
		bot.keyRelease(KeyEvent.VK_N);
		bot.delay(500);
		assertTrue(gamePanel.getGame().getTimeRemaining().intValue() >= 58);
		assertEquals(0, gamePanel.getGame().getGoal().intValue());
		for (GamePlayer player : gamePanel.getGame().getGamePlayers()) {
			assertEquals(0, player.getPlayerStatistics().intValue());
		}
	}
	
	/**
	 * Attempt to move both players in every direction and check that their position is correct.
	 */
	@Test
	void movePlayersTest() {
		int x, y;
		for(GamePlayer player : gamePanel.getGame().getGamePlayers()) {
			x = player.getPlayerPosition().x;
			y = player.getPlayerPosition().y;
			
			player.moveUp();
			assertTrue(player.getPlayerPosition().y < y);
			assertEquals(player.getPlayerPosition().x, x);
			y = player.getPlayerPosition().y;
			
			player.moveDown();
			assertTrue(player.getPlayerPosition().y > y);
			assertEquals(player.getPlayerPosition().x, x);
			y = player.getPlayerPosition().y;
			
			player.moveLeft();
			assertTrue(player.getPlayerPosition().x < x);
			assertEquals(player.getPlayerPosition().y, y);
			x = player.getPlayerPosition().x;

			player.moveRight();
			assertTrue(player.getPlayerPosition().x > x);
			assertEquals(player.getPlayerPosition().y, y);
			x = player.getPlayerPosition().x;
		}
	}
	
	/**
	 * Create new players using PlayerFactory, add them to the PlayerCollection, then remove players.
	 * Verify the size of the collection remains correct at each step. 
	 */
	@Test
	void collectionAddRemoveTest() {
		PlayerFactory factory = new PlayerFactory();
		PlayerCollection pc = gamePanel.getGame().getGamePlayers();
		pc.add(factory.getPlayer("striker"));
		pc.add(factory.getPlayer("goalkeeper"));
		pc.add(factory.getPlayer("striker"));
		assertEquals(5, pc.size());
		pc.remove(1);
		pc.remove(0);
		assertEquals(3, pc.size());
		pc.remove(pc.get("Striker"));
		assertEquals(2, pc.size());
	}
	
	/**
	 * Attempt to find elements that are and are not in the PlayerCollection. Verify it is handled well.
	 */
	@Test
	void collectionRetrievalTest() {
		PlayerCollection pc = gamePanel.getGame().getGamePlayers();
		GamePlayer striker = pc.get("Striker");
		assertEquals(striker, pc.get(pc.indexOf(striker)));
		GamePlayer goalkeeper = new PlayerFactory().getPlayer("goalkeeper");
		assertNull(pc.get(goalkeeper));
		assertNotEquals(-1, pc.indexOf(goalkeeper));
		try {
			pc.get(-1);
			fail();
		} catch (NoSuchElementException e) { /* Supposed to result in an exception */ }
		try {
			pc.get(pc.size());
			fail();
		} catch (NoSuchElementException e) { /* Supposed to result in an exception */ }
		try {
			pc.add(goalkeeper);
			pc.get(3);
			fail();
		} catch (NoSuchElementException e) { /* Supposed to result in an exception */ }
		assertFalse(pc.remove(pc.size()));
	}

}
