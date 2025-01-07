package org.RokueLike.domain.model.entity.monster;

import org.RokueLike.domain.hall.HallGrid;
import org.RokueLike.utils.Direction;

import java.io.Serial;
import java.io.Serializable;

public class Arrow implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L; // Serialization identifier

    private int x, y; // Current position of the arrow
    private final Direction direction; // Direction of the arrow's movement
    private final int range; // Maximum distance the arrow can travel
    private final HallGrid hallGrid; // The grid where the arrow exists
    private boolean active; // Indicates if the arrow is still active
    private int distanceTraveled; // Distance the arrow has traveled so far

    public Arrow(Monster archer, Direction direction, int range, HallGrid grid) {
        this.x = archer.getPositionX();
        this.y = archer.getPositionY();
        this.direction = direction;
        this.range = range;
        this.hallGrid = grid;
        this.active = true;
        this.distanceTraveled = 0;
    }

    // Moves the arrow one step in its direction and checks for collisions or range limits.
    // Deactivates the arrow if it hits an obstacle or exceeds its range.
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