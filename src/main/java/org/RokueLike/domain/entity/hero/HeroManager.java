package org.RokueLike.domain.entity.hero;

import org.RokueLike.domain.GameManager;
import org.RokueLike.domain.entity.item.Door;
import org.RokueLike.domain.hall.GridCell;
import org.RokueLike.domain.hall.HallGrid;
import org.RokueLike.domain.hall.HallManager;
import org.RokueLike.ui.Window;
import org.RokueLike.ui.screen.GameOverScreen;

import javax.swing.*;

public class HeroManager {

    private Hero hero;
    private HallGrid hallGrid;

    public HeroManager(Hero hero, HallGrid hallGrid) {
        this.hero = hero;
        this.hallGrid = hallGrid;
    }

    public void moveHero(HallManager hallManager, int directionX, int directionY) {
        GridCell cellInFront = hallGrid.getCellInFront(hero, directionX, directionY);

        if (cellInFront.getName().equals("floor")) {
            if (hallGrid.isSafeLocation(cellInFront.getPositionX(), cellInFront.getPositionY())) {
                hero.setPosition(hero.getPositionX() + directionX, hero.getPositionY() + directionY);
            }
        } else if (cellInFront.getName().equals("door")) {
            Door door = (Door) cellInFront;
            if (door.isOpen()) {
                if (hallManager.moveToNextHall()) {
                    HallGrid nextHall = hallManager.getCurrentHall();
                    GameManager.updateCurrentHall(nextHall);
                } else {
                    GameManager.reset();
                    Window.addScreen(new GameOverScreen("Congratulations, you WON!"), "GameOverScreen", true);
                }
            }
        }
    }

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
                GameManager.reset();
                Window.addScreen(new GameOverScreen("Game Over! Could not find safe location for hero"), "GameOverScreen", true);
                return;
            }
        }

        hero.setImmune(true);
        Timer immuneTimer = new Timer(5000, e -> {
            hero.setImmune(false);
        });
        immuneTimer.setRepeats(false);
        immuneTimer.start();
    }

    public boolean isAdjacentTo(int x, int y) {
        return (Math.abs(hero.getPositionX() - x) <= 1 && Math.abs(hero.getPositionY() - y) <= 1);
    }
}