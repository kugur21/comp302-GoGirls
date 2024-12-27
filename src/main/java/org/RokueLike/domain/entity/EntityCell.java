package org.RokueLike.domain.entity;

import org.RokueLike.domain.hall.GridCell;
import org.RokueLike.utils.Direction;

public class EntityCell extends GridCell {

    private Direction facing;


    public EntityCell(String name, int x, int y) {
        super(name, x, y);
        this.facing = Direction.LEFT;
    }

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