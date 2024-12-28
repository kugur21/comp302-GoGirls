package org.RokueLike.domain.entity;

import org.RokueLike.domain.hall.GridCell;
import org.RokueLike.utils.Direction;

public class EntityCell extends GridCell {

    private Direction facing; // Direction the entity is facing

    // Represents a grid cell occupied dynamic/moving being.
    public EntityCell(String name, int x, int y) {
        super(name, x, y);
    }

    // Updates the position of the entity.
    public void setPosition(int dirX, int dirY) {
        setPositionX(dirX);
        setPositionY(dirY);
    }

    public Direction getFacing() {
        return facing;
    }

    public void setFacing(Direction facing) {
        this.facing = facing;
    }

}