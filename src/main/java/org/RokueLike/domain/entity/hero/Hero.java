package org.RokueLike.domain.entity.hero;

import org.RokueLike.domain.entity.EntityCell;
import org.RokueLike.domain.entity.item.Enchantment.EnchantmentType;

import java.util.List;

public class Hero extends EntityCell {

    private static final int MAX_LIVES = 4;
    private static final int MAX_TIME = 60;

    private int lives;
    private int remainingTime;
    private boolean immune = false;
    private final Inventory inventory;

    //INFORMATION EXPERT INSTANCE - The Hero class handles responsibilities related to hero-specific data.
    //MODEL-VIEW SEPARATION PRINCIPLE - Domain classes (Hero, Monster, BuildManager, etc.) handle the core game logic and data manipulation independently of the UI

    public Hero(int x, int y) {
        super("hero", x, y);
        this.lives = MAX_LIVES;
        this.remainingTime = MAX_TIME;
        this.inventory = new Inventory();
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

    public int getLives() {
        return lives;
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

    public int getRemainingTime() {
        return this.remainingTime;
    }

    public boolean isAlive() {
        return lives > 0 && remainingTime > 0;
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

    public Inventory getInventory() {
        return this.inventory;
    }

    public boolean isImmune() {
        return immune;
    }

    public void setImmune(boolean immune) {
        this.immune = immune;
    }

}