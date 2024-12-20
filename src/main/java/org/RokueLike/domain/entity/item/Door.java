package org.RokueLike.domain.entity.item;

public class Door extends Item {

    private boolean isOpen;

    public Door(int x, int y) {
        super("door", x, y, ItemType.DOOR);
        this.isOpen = false;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void open() {
        this.isOpen = true;
    }
}