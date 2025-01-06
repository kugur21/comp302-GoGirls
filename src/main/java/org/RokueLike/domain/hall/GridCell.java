package org.RokueLike.domain.hall;

import java.io.Serial;
import java.io.Serializable;
public class GridCell implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L; // Serialization identifier

    private final String name; // Name of the cell (e.g., wall, floor, etc.)

    // Coordinates of the cell
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

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }
}