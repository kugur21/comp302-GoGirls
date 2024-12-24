package org.RokueLike.domain.entity.item;

import org.RokueLike.domain.utils.Direction;

public class Arrow {
    private int x, y;
    private final Direction direction;
    private boolean active;

    public Arrow(int startX, int startY, Direction direction) {
        this.x = startX;
        this.y = startY;
        this.direction = direction;
        this.active = true;
    }

    public void move() {
        switch (direction) {
            case UP -> y--;
            case DOWN -> y++;
            case LEFT -> x--;
            case RIGHT -> x++;
        }
    }

    public boolean checkCollision(int targetX, int targetY) {
        return this.x == targetX && this.y == targetY;
    }

    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        this.active = false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
