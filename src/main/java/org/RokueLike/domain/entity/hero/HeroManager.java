package org.RokueLike.domain.entity.hero;

import org.RokueLike.domain.GameManager;
import org.RokueLike.domain.entity.item.Door;
import org.RokueLike.domain.hall.GridCell;
import org.RokueLike.domain.hall.HallGrid;
import org.RokueLike.domain.hall.HallManager;

public class HeroManager {

    private Hero hero;
    private HallGrid hallGrid;

    public HeroManager(Hero hero, HallGrid hallGrid) {
        this.hero = hero;
        this.hallGrid = hallGrid;
    }

    public boolean moveHero(HallManager hallManager, int directionX, int directionY) {
        GridCell cellInFront = hallGrid.getCellInFront(hero, directionX, directionY);
        switch (cellInFront.getName()) {
            case "floor":
                hero.setPosition(hero.getPositionX() + directionX, hero.getPositionY() + directionY, true);
                return true;
            case "door":
                Door door = (Door) cellInFront;
                if (door.isOpen()) {
                    if (hallManager.moveToNextHall()) {
                        HallGrid nextHall = hallManager.getCurrentHall();
                        GameManager.updateCurrentHall(nextHall);
                        return true;
                    } else {
                        System.out.println("Congrats, you have escaped the dungeon!");
                        System.exit(0);
                        return false;
                    }
                } else {
                    return false;
                }
            case "wall":
                return false;
            case "object":
                return false;
            default:
                return false;
        }
    }

    public String respawnHero() {
        int startX = hallGrid.getStartX();
        int startY = hallGrid.getStartY();

        if (hallGrid.isSafeLocation(startX, startY)) {
            hero.setPosition(startX, startY, false);
            return "Hero respawned at starting position: (" + startX + ", " + startY + ")";
        } else {
            int[] safeLocation = hallGrid.findRandomSafeCell();
            if (safeLocation != null) {
                hero.setPosition(safeLocation[0], safeLocation[1], false);
                return "Hero respawned at safe position: (" + safeLocation[0] + ", " + safeLocation[1] + ")";
            } else {
                System.exit(0);
                return "No safe location found. Game over!";
            }
        }
    }

    public boolean isAdjacentTo(int x, int y) {
        return (Math.abs(hero.getPositionX() - x) <= 1 && Math.abs(hero.getPositionY() - y) <= 1);
    }
}