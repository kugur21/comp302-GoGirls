
package org.RokueLike.domain.entity.hero;
import org.RokueLike.domain.GameManager;
import org.RokueLike.domain.entity.EntityCell;
import org.RokueLike.domain.entity.item.Enchantment.EnchantmentType;
import org.RokueLike.ui.Window;
import org.RokueLike.ui.screen.GameOverScreen;

public class Hero extends EntityCell {

    private static final int MAX_LIVES = 4;
    private static final int MAX_TIME = 60;

    private int lives;
    private int remainingTime;
    private final Inventory inventory;
    private boolean immune = false;


    //INFORMATION EXPERT INSTANCE - The Hero class handles responsibilities related to hero-specific data.
    //MODEL-VIEW SEPARATION PRINCIPLE - Domain classes (Hero, Monster, BuildManager, etc.) handle the core game logic and data manipulation independently of the UI

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
    public boolean isAlive() {
        return lives > 0 && remainingTime > 0;
    }

    public boolean isImmune() {
        return immune;
    }

    public void setImmune(boolean immune) {
        this.immune = immune;
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

    public Inventory getInventory() {
        return this.inventory;
    }

}
