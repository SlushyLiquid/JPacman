package nl.tudelft.jpacman.systemTest;

import nl.tudelft.jpacman.Launcher;
import nl.tudelft.jpacman.board.Board;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.game.Game;
import nl.tudelft.jpacman.level.Pellet;
import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.ui.PacManUI;
import nl.tudelft.jpacman.ui.PacManUiBuilder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SystemSmokeTest {

    private Game game;
    private PacManUI ui;
    private Player player;

    @BeforeEach
    public void setup() {
        // Set up game and UI before each test
        Launcher launcher = new Launcher();
        game = launcher.makeGame(); // This is public
        player = game.getPlayers().get(0);
        ui = new PacManUiBuilder().build(game);
        ui.start();
    }

    @Test
    public void testGameStarts() throws Exception {
        // Verify game is initially not running
        assertNotNull(game);
        assertFalse(game.isInProgress(), "Game should not be running initially.");

        // Start the game
        game.start();
        Thread.sleep(500); // Give the game time to update

        // Confirm game has started
        assertTrue(game.isInProgress(), "Game should be running after start.");
    }

    @Test
    public void testGameStops() throws Exception {
        // Start the game first
        game.start();
        assertTrue(game.isInProgress());

        // Stop the game
        game.stop();
        // Confirm it has stopped
        assertFalse(game.isInProgress());
    }

    @Test
    public void testMovePacman() throws Exception {
        game.start();
        assertTrue(game.isInProgress());

        // Simulate right arrow key press to move Pacman
        Robot robot = new Robot();
        robot.setAutoDelay(3000);
        robot.keyPress(KeyEvent.VK_RIGHT);
        robot.keyRelease(KeyEvent.VK_RIGHT);

        // Wait for movement to register
        Thread.sleep(500);

        // Assert Pacman has moved
        assertNotNull(player.getDirection(), "Player should have a direction after movement.");
    }

    @Test
    public void testGameLoss() throws Exception {
        game.start();

        // Wait for ghost collision
        Thread.sleep(5000); // Adjust time as needed for the map setup

        // Confirm player died and game ended
        assertFalse(player.isAlive(), "Player should be dead after ghost collision.");
        assertFalse(game.isInProgress(), "Game should end after losing.");
    }

    @Test
    public void testLevelCompletionByRemovingPellets() throws Exception {
        // Start the game
        game.start();
        assertTrue(game.isInProgress());

        Board board = game.getLevel().getBoard();

        // Remove all pellets from the board manually
        for (int x = 0; x < board.getWidth(); x++) {
            for (int y = 0; y < board.getHeight(); y++) {
                Square square = board.squareAt(x, y);
                List<Unit> occupants = new ArrayList<>(square.getOccupants());
                for (Unit unit : occupants) {
                    if (unit instanceof Pellet) {
                        unit.leaveSquare(); // This removes the pellet
                    }
                }
            }
        }

        Thread.sleep(500); // Allow the game engine to update
        // Game should now be over due to level completion
        assertFalse(game.isInProgress(), "Game should end after all pellets are removed.");
        assertTrue(player.isAlive(), "Player should still be alive after completing the level.");
    }
}