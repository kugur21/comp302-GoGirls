package org.RokueLike.domain.entity;

import org.RokueLike.domain.hall.GridCell;
import org.RokueLike.domain.utils.Direction;

public class EntityCell extends GridCell {

    private Direction facing;

    private int motionOffsetX;
    private int motionOffsetY;

    public EntityCell(String name, int x, int y) {
        super(name, x, y);
        this.facing = Direction.LEFT;
    }

    public void setPosition(int dirX, int dirY, boolean animated) {
        if (animated) {
            if (dirX > positionX) motionOffsetX = 32;
            else if (dirX < positionX) motionOffsetX = -32;
            else if (dirY > positionY) motionOffsetY = 32;
            else if (dirY < positionY) motionOffsetY = -32;
        }
        setPositionX(dirX);
        setPositionY(dirY);
    }

    public Direction getFacing() {
        return facing;
    }

    public void setFacing(Direction facing) {
        this.facing = facing;
    }

    public int getMotionOffsetX() {
        return motionOffsetX;
    }

    public int getMotionOffsetY() {
        return motionOffsetY;
    }

    public void setMotionOffset(int motionOffsetX, int motionOffsetY) {
        this.motionOffsetX = motionOffsetX;
        this.motionOffsetY = motionOffsetY;
    }

    public void decreaseMotionOffset() {
        if (motionOffsetX > 0) motionOffsetX -= 2;
        else if (motionOffsetX < 0) motionOffsetX += 2;
        if (motionOffsetY > 0) motionOffsetY -= 2;
        else if (motionOffsetY < 0) motionOffsetY += 2;
    }

}