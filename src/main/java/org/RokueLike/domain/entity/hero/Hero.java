
package org.RokueLike.domain.entity.hero;

import org.RokueLike.domain.entity.EntityCell;
import org.RokueLike.domain.entity.item.Enchantment.EnchantmentType;

import java.util.List;

public class Hero extends EntityCell {

    private int lives;
    private int remainingTime;
    private final Inventory inventory;

    public Hero(int x, int y) {
        super("hero", x, y);
        this.lives = 3;
        this.remainingTime = 30;
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
        if (lives < 4) {
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

    public boolean isAlive() {
        return lives > 0 && remainingTime > 0;
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


    // Inventory getter method added for PlayModeRenderer
    public Inventory getInventory() {
        return this.inventory;
    }












}
