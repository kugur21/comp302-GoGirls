package org.RokueLike.domain.entity.item;

import org.RokueLike.domain.entity.monster.Monster;
import org.RokueLike.utils.Direction;

public class Arrow {
    private int x, y;
    private final Direction direction;
    private final int range;
    private boolean active;
    private int distanceTraveled;

    public Arrow(Monster archer, Direction direction, int range) {
        this.x = archer.getPositionX();
        this.y = archer.getPositionY();
        this.direction = direction;
        this.range = range;
        this.active = true;
        this.distanceTraveled = 0;
    }

    public void move() {
        switch (direction) {
            case UP -> y--;
            case DOWN -> y++;
            case LEFT -> x--;
            case RIGHT -> x++;
        }
        distanceTraveled++;
        if (distanceTraveled >= range) {
            deactivate();
        }
    }

    public boolean notActive() {
        return !active;
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

    public Direction getDirection() {
        return direction;
    }

}