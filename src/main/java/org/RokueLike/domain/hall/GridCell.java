package org.RokueLike.domain.hall;

public class GridCell {

    private String name;

    protected int positionX;
    protected int positionY;

    public GridCell(String name, int positionX, int positionY) {
        this.name = name;
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public String getName() {
        return name;
    }

    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }
}
