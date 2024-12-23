
package org.RokueLike.domain.entity.hero;

import org.RokueLike.domain.entity.EntityCell;
import org.RokueLike.domain.entity.item.Enchantment.EnchantmentType;

public class Hero extends EntityCell {

    private static final int MAX_LIVES = 4;
    private static final int MAX_TIME = 60;

    private int lives;
    private int remainingTime;
    private final Inventory inventory;

    public Hero(int x, int y) {
        super("hero", x, y);
        this.lives = MAX_LIVES;
        this.remainingTime = MAX_TIME;
        this.inventory = new Inventory();
    }

    public boolean hasEnchantment(EnchantmentType enchantment) {
        return inventory.hasEnchantment(enchantment);
    }

    public void useEnchantment(EnchantmentType enchantment) {
        inventory.removeItem(enchantment);
    }

    public void addToInventory(EnchantmentType enchantment) {
        inventory.addItem(enchantment);
    }

    public int getLives() {
        return lives;
    }

    public void incrementLives() {
        if (lives < MAX_LIVES) {
            this.lives++;
        }
    }

    public void decrementLives() {
        if (lives > 0) {
            this.lives--;
        }
    }

    public int getRemainingTime() {
        return this.remainingTime;
    }

    public void addRemainingTime(int seconds) {
        this.remainingTime += seconds;
    }

    public void decrementRemainingTime() {
        this.remainingTime--;
    }

    public void resetRemainingTime() {
        this.remainingTime = MAX_TIME;
    }

    public boolean notAlive() {
        return lives <= 0 || remainingTime <= 0;
    }

    public Inventory getInventory() {
        return this.inventory;
    }

}
