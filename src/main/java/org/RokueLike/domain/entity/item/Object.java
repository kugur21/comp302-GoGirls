package org.RokueLike.domain.entity.item;

public class Object extends Item {

    private boolean isOpen;
    private boolean containsRune;

    public Object(String name, int x, int y) {
        super(name, x, y, ItemType.OBJECT);
        this.isOpen = false;
        this.containsRune = false;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void open() {
        this.isOpen = true;
    }

    public void close() {
        this.isOpen = false;
    }

    public boolean containsRune() {
        return containsRune;
    }

    public void setContainedRune() {
        this.containsRune = true;
    }

    public void removeContainedRune() {
        this.containsRune = false;
    }

}