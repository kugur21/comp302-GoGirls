package org.RokueLike.domain.manager;

import org.RokueLike.domain.GameManager;
import org.RokueLike.domain.model.entity.hero.Hero;
import org.RokueLike.domain.model.item.Door;
import org.RokueLike.domain.hall.GridCell;
import org.RokueLike.domain.hall.HallGrid;
import org.RokueLike.ui.Window;
import org.RokueLike.ui.screen.GameOverScreen;

import javax.swing.*;
import java.io.Serial;
import java.io.Serializable;
import static org.RokueLike.utils.Constants.*;

public class HeroManager implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L; // Serialization identifier

    private final Hero hero; // The hero instance
    private final HallGrid hallGrid; // The current hall grid

    public HeroManager(Hero hero, HallGrid hallGrid) {
        this.hero = hero;
        this.hallGrid = hallGrid;
    }

    /**
     * Requires:
     * - A valid instance of `HallManager` to manage the grid and hall details.
     * - `directionX` and `directionY` are integers representing the relative movement
     *   direction; they must correspond to valid offsets within the grid's bounds.
     * Modifies:
     * - Updates the position of the `hero` object if the conditions for movement are met.
     * Effects:
     * - Determines the cell in front of the hero based on the provided direction.
     * - If the cell is a "floor" and deemed safe, the hero's position is updated.
     * - No changes occur if the cell is not traversable or the direction is invalid.
     */
    public void moveHero(HallManager hallManager, int directionX, int directionY) {
        GridCell cellInFront = hallGrid.getCellInFront(hero, directionX, directionY);

        if (cellInFront.getName().equals("floor")) {
            // Move hero if the floor is safe and movable
            if (hallGrid.isSafeLocation(cellInFront.getPositionX(), cellInFront.getPositionY())) {
                hero.setPosition(hero.getPositionX() + directionX, hero.getPositionY() + directionY);
            }
        } else if (cellInFront.getName().equals("door")) {
            // Handle door interactions
            Door door = (Door) cellInFront;
            if (door.isOpen()) {
                if (hallManager.moveToNextHall()) {
                    handleHallTransition(hallManager.getCurrentHall());
                } else {
                    handleGameWin();
                }
            }
        }
    }

    // Progress to the next hall
    public void handleHallTransition(HallGrid nextHall) {
        GameManager.updateCurrentHall(nextHall);
    }

    // Victory scenario
    public void handleGameWin() {
        GameManager.reset(true);
        Window.addScreen(new GameOverScreen("Congratulations, you WON!"), "GameOverScreen", true);
    }

    // Respawns the hero at a safe location or ends the game if no safe location is found.
    public void respawnHero() {
        int startX = hallGrid.getStartX();
        int startY = hallGrid.getStartY();

        if (hallGrid.isSafeLocation(startX, startY)) {
            hero.setPosition(startX, startY);
        } else {
            int[] safeLocation = hallGrid.findRandomSafeCell();
            if (safeLocation != null) {
                hero.setPosition(safeLocation[0], safeLocation[1]);
            } else {
                GameManager.reset(true); // Game over if no safe location is found
                Window.addScreen(new GameOverScreen("Game Over! Could not find safe location for hero"), "GameOverScreen", true);
                return;
            }
        }

        // Apply temporary immunity
        hero.setImmune(true);
        Timer immuneTimer = new Timer(IMMUNE_TIME, e -> {
            hero.setImmune(false);
        });
        immuneTimer.setRepeats(false);
        immuneTimer.start();
    }

    // Checks if the hero is adjacent to the specified coordinates.
    public boolean isAdjacentTo(int x, int y) {
        return (Math.abs(hero.getPositionX() - x) <= 1 && Math.abs(hero.getPositionY() - y) <= 1);
    }
}