package org.RokueLike.domain.entity.item;

import org.RokueLike.domain.hall.GridCell;

public class Door extends GridCell {

    private boolean isOpen; // Indicates if the door is open

    public Door(int x, int y) {
        super("door", x, y);
        this.isOpen = false;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void open() {
        this.isOpen = true;
    }
}