package org.RokueLike.domain.entity.hero;

import org.RokueLike.domain.entity.EntityCell;
import org.RokueLike.domain.entity.item.Enchantment.EnchantmentType;

public class Hero extends EntityCell {

    private int lives;
    private Inventory inventory;

    public Hero(int x, int y) {
        super("hero", x, y);
        this.lives = 3;
        this.inventory = new Inventory();
    }

    public boolean hasEnchantment(EnchantmentType enchantment) {
        return inventory.hasEnchantment(enchantment);
    }

    public void useEnchantment(EnchantmentType enchantment) {
        inventory.removeItem(enchantment);
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public void incrementLives() {
        if (lives < 4) {
            this.lives++;
        }
    }

    public void decrementLives() {
        if (lives > 0) {
            this.lives--;
        }
    }

    public boolean isAlive() {
        return lives > 0;
    }

    public Inventory getInventory() {
        return inventory;
    }

}
