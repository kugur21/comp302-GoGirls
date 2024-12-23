package org.RokueLike.domain.entity;

public class Arrow {
    private int startX, startY;
    private int targetX, targetY;
    private int currentX, currentY;

    public Arrow(int startX, int startY, int targetX, int targetY) {
        this.startX = startX;
        this.startY = startY;
        this.targetX = targetX;
        this.targetY = targetY;
        this.currentX = startX;
        this.currentY = startY;
    }

    public void move() {
        // Logic to move the arrow step by step toward the target
        if (currentX < targetX) currentX++;
        else if (currentX > targetX) currentX--;
        if (currentY < targetY) currentY++;
        else if (currentY > targetY) currentY--;
    }

    public boolean hasReachedTarget() {
        return currentX == targetX && currentY == targetY;
    }

    public int getCurrentX() {
        return currentX;
    }

    public int getCurrentY() {
        return currentY;
    }

    // Add these getter methods for startX and startY
    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int getTargetX() {
        return targetX;
    }

    public int getTargetY() {
        return targetY;
    }
}
