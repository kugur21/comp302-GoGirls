
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
            System.out.println("Hero gained an extra life! Lives: " + lives);
        }
    }

    public void decrementLives() {
        if (lives > 0) {
            this.lives--;
            System.out.println("Hero lost a life! Lives remaining: " + lives);
        }
    }

    public boolean notAlive() {
        return lives <= 0 || remainingTime <= 0;
    }

    public int getRemainingTime() {
        return this.remainingTime;
    }

    public void addRemainingTime(int seconds) {
        this.remainingTime += seconds;
        System.out.println("Hero gained " + seconds + " seconds. Total time: " + remainingTime + " seconds.");
    }

    public void decrementRemainingTime() {
        this.remainingTime--;
    }

    public void resetRemainingTime() {
        this.remainingTime = MAX_TIME;
    }


    // Inventory getter method added for PlayModeRenderer
    public Inventory getInventory() {
        return this.inventory;
    }

    private int cloakTimer = 0;

    public boolean isCloakActive() {
        return cloakTimer > 0;
    }

    public void activateCloak(int duration) {
        this.cloakTimer = duration;
    }

    public void updateCloakTimer() {
        if (cloakTimer > 0) cloakTimer--;
    }

    public int getCloakTimer() {
        return cloakTimer;
    }


}
