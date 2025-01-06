package org.RokueLike.domain.model.entity.hero;

import org.RokueLike.domain.model.entity.EntityCell;
import org.RokueLike.domain.model.item.Enchantment.EnchantmentType;
import static org.RokueLike.utils.Constants.*;

public class Hero extends EntityCell {

    private int lives; // Current number of lives
    private int remainingTime; // Remaining time for the hero
    private final Inventory inventory; // Hero's inventory for enchantments
    private transient boolean immune = false; // Immunity status of the hero


    /// / INFORMATION EXPERT INSTANCE - The Hero class handles responsibilities related to hero-specific data.
    /// / MODEL-VIEW SEPARATION PRINCIPLE - Domain classes (Hero, Monster, BuildManager, etc.) handle the core game logic and data manipulation independently of the UI

    public Hero(int x, int y) {
        super("hero", x, y);
        this.lives = MAX_LIVES;
        this.remainingTime = MAX_TIME;
        this.inventory = new Inventory();
    }

    // Remaining lives modifications
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

    // Remaining time modifications
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

    // Inventory modifications
    public Inventory getInventory() {
        return this.inventory;
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


    public boolean isImmune() {
        return immune;
    }

    public void setImmune(boolean immune) {
        this.immune = immune;
    }

    // Checks if the hero is still alive based on lives and remaining time.
    public boolean isAlive() {
        return lives > 0 && remainingTime > 0;
    }
}