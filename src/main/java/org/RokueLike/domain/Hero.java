package org.RokueLike.domain;

public class Hero {
    private int x, y; // Position
    private int health;
    private int strength;

    public Hero(int x, int y, int health, int strength) {
        this.x = x;
        this.y = y;
        this.health = health;
        this.strength = strength;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getHealth() {
        return health;
    }

    public void decreaseHealth(int amount) {
        health -= amount;
        if (health < 0) health = 0;
    }

    public int getStrength() {
        return strength;
    }
}
