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

        // Finished?

    }

    /**
     * Handles interactions with objects in the hero's current cell.
     * @param hallGrid The current hall grid.
     */
    public void interactWithCell(HallGrid hallGrid) {
        // Implementation will go here.
    }

    /**
     * Applies an enchantment effect to the hero.
     * @param enchantmentName The name of the enchantment to apply.
     */
    public void applyEnchantment(String enchantmentName) {
        // Implementation will go here.
    }

    /**
     * Uses an item from the hero's inventory.
     * @param itemName The name of the item to use.
     * @return True if the item was successfully used, false otherwise.
     */
    public boolean useItem(String itemName) {
        // Implementation will go here.
        return false;
    }

    /**
     * Adds extra time to the hero's timer.
     * @param extraTime The amount of extra time to add.
     */
    public void addTime(int extraTime) {
        // Implementation will go here.
    }

    /**
     * Checks if the hero has completed the current hall.
     * @param hallGrid The current hall grid.
     * @return True if the hero has found the rune, false otherwise.
     */
    public boolean checkHallCompletion(HallGrid hallGrid) {
        // Implementation will go here.
        return false;
    }

}
