package org.RokueLike.domain.entity.hero;

import org.RokueLike.domain.entity.EntityCell;

public class Hero extends EntityCell {

    private int lives;
    private Inventory inventory;

    public Hero(int x, int y, int lives) {
        super("hero", x, y);
        this.lives = lives;
        this.inventory = new Inventory();
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
