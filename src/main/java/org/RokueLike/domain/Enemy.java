package org.RokueLike.domain;

public class Enemy {
    private int x, y; // Position

    public Enemy(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void moveTowards(int targetX, int targetY) {
        if (x < targetX) x++;
        else if (x > targetX) x--;

        if (y < targetY) y++;
        else if (y > targetY) y--;
    }
}
