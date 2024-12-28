package org.RokueLike.domain.entity.item;

import org.RokueLike.domain.entity.monster.Monster;
import org.RokueLike.domain.hall.HallGrid;
import org.RokueLike.utils.Direction;

public class Arrow {
    private int x, y;
    private final Direction direction;
    private final int range;
    private final HallGrid hallGrid;
    private boolean active;
    private int distanceTraveled;

    public Arrow(Monster archer, Direction direction, int range, HallGrid grid) {
        this.x = archer.getPositionX();
        this.y = archer.getPositionY();
        this.direction = direction;
        this.range = range;
        this.hallGrid = grid;
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
        if (hallGrid.isSafeLocation(x, y)) {
            distanceTraveled++;
            if (distanceTraveled >= range) {
                deactivate();
            }
        } else {
            deactivate();
        }
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

    public Direction getDirection() {
        return direction;
    }

}